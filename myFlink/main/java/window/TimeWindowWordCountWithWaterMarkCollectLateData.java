package window;

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 每个3秒统计前3秒内相同key的所有事件
 *
 * 那么watermark时间怎么计算呢？
 *      用当前窗口最大事件时间 - 允许延迟时间
 *
 * -- window 计算触发的条件
 * 000001,1461756862000         --|19:34:22|19:34:22|19:34:12  事件时间是 22
 * 000001,1461756866000         --|19:34:26|19:34:26|19:34:16
 * 000001,1461756872000
 * 000001,1461756873000
 * 000001,1461756874000         --wartermark时间=24 输到这条时才触发，触发 21s - 24s 的窗口的数据
 * 000001,1461756876000
 * 000001,1461756877000         --watermark时间=27  会把小于27窗口的所有事件都触发
 *
 * 发现触发时间，和当前时间没关系，和事件时间没关系，只和watermark时间有关！！！
 *
 *
 * flink,1461756879000
 * flink,1461756871000
 * flink,1461756883000
 * 这个例子，尽管第二条乱序，还是正确计算出来了
 *
 * flink,1461756870000
 * flink,1461756883000
 * flink,1461756870000
 * flink,1461756871000
 * flink,1461756872000
 * 这几组数据：对于迟到太多的数据就丢弃了
 *      比如 watermark = 33s, 触发之前的，
 *      那么再来 事件时间 < 33s的 就不会再触发了，就丢弃了
 */
public class TimeWindowWordCountWithWaterMark2 {
    public static void main(String[] args) throws Exception {
        //步骤一、获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        //步骤一：设置时间类型
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        //设置watermark产生的周期为1s
        env.getConfig().setAutoWatermarkInterval(1000);
        //步骤三、
        DataStreamSource<String> dataStream = env.socketTextStream("bigdata02", 8888);

        SingleOutputStreamOperator<String> result = dataStream.map(new MapFunction<String, Tuple2<String, Long>>() {
            @Override
            public Tuple2<String, Long> map(String s) throws Exception {
                String[] fields = s.split(",");
                return new Tuple2<String, Long>(fields[0], Long.valueOf(fields[1]));
            }
            //步骤二：获取数据里面的 event Time
        }).assignTimestampsAndWatermarks(new EventTimeExtractor())
                .keyBy(0)
                .timeWindow(Time.seconds(3))
                .allowedLateness(Time.seconds(2))   //允许在延迟10秒的基础上再延迟2s
                    //所有符合条件的，每次都打印出来
                .process(new SumProcessWindowFunction());//.print().setParallelism(1);

        result.print().setParallelism(1);
        env.execute("TimeWindowWordCount With WaterMark");
    }

    public static class SumProcessWindowFunction extends
            ProcessWindowFunction<Tuple2<String,Long>, String, Tuple, TimeWindow> {
        FastDateFormat dataFormat = FastDateFormat.getInstance("HH:mm:ss");
        /**
         * 当一个window触发计算的时候会调用这个方法
         * @param tuple key
         * @param context operator的上下文
         * @param elements 指定window的所有元素
         * @param out 用户输出
         */
        @Override
        public void process(Tuple tuple, Context context,
                            Iterable<Tuple2<String, Long>> elements,
                            Collector<String> out) {
            System.out.println("处理时间：" + dataFormat.format(context.currentProcessingTime()));
            System.out.println("Window开始时间："+dataFormat.format(context.window().getStart()));

            List<String> list = new ArrayList<>();
            for (Tuple2<String, Long> ele : elements) {
                list.add(ele.toString() + "|" + dataFormat.format(ele.f1));
            }
            out.collect(list.toString());
            System.out.println("window结束时间" + context.window().getEnd());
        }
    }
    private static class EventTimeExtractor implements AssignerWithPeriodicWatermarks<Tuple2<String, Long>> {
        FastDateFormat dateFormat = FastDateFormat.getInstance("HH:mm:ss");

        private long currentMaxEventTime = 0L;
        private long maxOutOfOrderness = 10000; //最大允许的乱序时间 初始值是 10秒

        @Nullable
        @Override
        public Watermark getCurrentWatermark() {
            //window 延迟5秒 触发
            //System.out.println("water mark ...");
            //用当前窗口最大事件时间 - 允许延迟时间
            return new Watermark(currentMaxEventTime - maxOutOfOrderness);
        }

        //指定时间的字段，注意：单位是毫秒
        @Override
        public long extractTimestamp(Tuple2<String, Long> element, long l) {
            long currentElementEventTime = element.f1;
            currentMaxEventTime = Math.max(currentMaxEventTime, currentElementEventTime);
            System.out.println("event = " + element
                    + "|" + dateFormat.format(element.f1)       //当前事件时间
                    + "|" + dateFormat.format(currentMaxEventTime)  //当前窗口最大事件时间
                    + "|" + dateFormat.format(getCurrentWatermark().getTimestamp()) //watermark时间
            );
            return currentElementEventTime;
        }
    }
}
