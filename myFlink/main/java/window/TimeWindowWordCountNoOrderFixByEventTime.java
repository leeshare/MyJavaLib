package window;

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.util.concurrent.TimeUnit;

/**
 * 第13秒 生成2个 2个 “hadoop”，但只发了1个，
 *      第15秒 统计（5-15内）得 1
 * 第16秒 发了1个 “hadoop”
 * 第19秒 发第13秒生成的一个 “hadoop”
 *      第20秒 统计（10-20内）得 3
 * 第25秒 统计（15-25内）得 2
 *
 * 这是有特殊情况的 计数
 * 即乱序了
 * （正常情况应该是：2 3 1）
 */
public class TimeWindowWordCountNoOrder {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        DataStreamSource<String> dataStream = env.addSource(new TestSource());
        //SingleOutputStreamOperator<Tuple2<String, Integer>> result =
                dataStream.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(String s, Collector<Tuple2<String, Integer>> collector) throws Exception {
                String[] fields = s.split(",");
                for (String word : fields) {
                    collector.collect(new Tuple2<>(word, 1));
                }

            }
        }).keyBy(0).timeWindow(Time.seconds(10), Time.seconds(5)).process(new SumProcessWindowFunction())
                        .print().setParallelism(1);
        //result.print().setParallelism(1);
        env.execute("TimeWindowWordCount In Order");
    }


    public static class TestSource implements SourceFunction<String> {
        FastDateFormat dateFormat = FastDateFormat.getInstance("HH:mm:ss");

        @Override
        public void run(SourceContext<String> ctx) throws Exception {
            //控制大约在 10秒的倍数时间点发送事件
            String currTime = String.valueOf(System.currentTimeMillis());
            while (Integer.valueOf(currTime.substring(currTime.length() - 4)) > 100) {
                currTime = String.valueOf(System.currentTimeMillis());
                continue;
            }
            System.out.println("开始发送事件的时间：" +
                    dateFormat.format(System.currentTimeMillis()));
// 第 13 秒发送两个事件
            TimeUnit.SECONDS.sleep(13);
            ctx.collect("hadoop," + System.currentTimeMillis());
// 产生了一个事件，但是由于网络原因，事件没有发送
            String event = "hadoop," + System.currentTimeMillis();
// 第 16 秒发送一个事件
            TimeUnit.SECONDS.sleep(3);
            ctx.collect("hadoop," + System.currentTimeMillis());
            TimeUnit.SECONDS.sleep(3);
            //第19秒才把 第13秒生成的事件发出去
            ctx.collect(event);
            TimeUnit.SECONDS.sleep(300);
        }

        @Override
        public void cancel() {

        }
    }

    public static class SumProcessWindowFunction extends
            ProcessWindowFunction<Tuple2<String,Integer>,Tuple2<String,Integer>, Tuple, TimeWindow> {
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
                            Iterable<Tuple2<String, Integer>> elements,
                            Collector<Tuple2<String, Integer>> out) {
            //System.out.println("当天系统的时间："+dataFormat.format(System.currentTimeMillis()));
            //System.out.println("Window的处理时间："+dataFormat.format(context.currentProcessingTime()));
            //System.out.println("Window的开始时间："+dataFormat.format(context.window().getStart()));
            //System.out.println("Window的结束时间："+dataFormat.format(context.window().getEnd()));
            int sum = 0;
            for (Tuple2<String, Integer> ele : elements) {
                sum += 1;
            }
// 输出单词出现的次数
            out.collect(Tuple2.of(tuple.getField(0), sum));
        }
    }
}
