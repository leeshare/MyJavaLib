package sensor;

import org.apache.flink.api.common.functions.*;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.collector.selector.OutputSelector;
import org.apache.flink.streaming.api.datastream.*;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.functions.AssignerWithPunctuatedWatermarks;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * 无法运行，只是联系一下 写法
 */
public class AverageSensorReadings {

    public static void main(String[] args) throws Exception {
        //实时环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //设置 event time
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        //设置数据源
        DataStreamSource<SensorReading> sensorData = env.addSource(new SensorSource());

        DataStreamSource<SensorReading> sensorData2 = env.addSource(new SensorSource());
        //map
        SingleOutputStreamOperator<SensorReading> mapData = sensorData.map(r -> {
            double celsius = (r.temperature - 32) * (5.0 / 9.0);
            return new SensorReading(r.id, r.timestamp, celsius);
        });
        //filter
        SingleOutputStreamOperator<SensorReading> filterData = mapData.filter(r -> r.temperature >= 25);
        //flatMap 假装id是一个字符串，然后将其拆分
        SingleOutputStreamOperator<Tuple2<String, Integer>> flatMapData = filterData.flatMap((SensorReading r, Collector<Tuple2<String, Integer>> out) -> Arrays.stream(r.id.split("")).forEach(a -> out.collect(Tuple2.of(a, 1))));
        //keyBy
        KeyedStream<SensorReading, String> keyedData = filterData.keyBy(r -> r.id);
        //reduce       这里实现了一个 类似于 max()的聚合
        keyedData.reduce((r1, r2) -> r1.temperature > r2.temperature ? r1 : r2);

        //union  两个流类型必须相同，返回此类型的流
        DataStream<SensorReading> unionStream = sensorData.union(sensorData2);

        SingleOutputStreamOperator<Tuple2<String, Integer>> flatMapStream2 = sensorData2.flatMap((SensorReading r, Collector<Tuple2<String, Integer>> out) -> Arrays.stream(r.id.split("")).forEach(x -> out.collect(Tuple2.of(x, 1))));
        //connect   两个流类型可以不相同，反正会把两个类型 一起返回
        ConnectedStreams<SensorReading, Tuple2<String, Integer>> connectStream = sensorData.connect(flatMapStream2);

        //select
        SplitStream<SensorReading> splitStream = sensorData.split(new OutputSelector<SensorReading>() {
            @Override
            public Iterable<String> select(SensorReading sensorReading) {
                ArrayList<String> out = new ArrayList<>();
                if (sensorReading.temperature >= 22)
                    out.add("hot");
                else
                    out.add("cold");
                return out;
            }
        });
        DataStream<SensorReading> selectStream = splitStream.select("hot");

        WindowedStream<SensorReading, String, TimeWindow> windowedData = keyedData.timeWindow(Time.seconds(5));
        SingleOutputStreamOperator<SensorReading> apply = windowedData.apply(new TemperatureAverager());
        DataStreamSink<SensorReading> print = apply.print();

        //对 source
        SingleOutputStreamOperator<SensorReading> w1 = sensorData.assignTimestampsAndWatermarks(new AssignerWithPeriodicWatermarks<SensorReading>() {
            @Nullable
            @Override
            public Watermark getCurrentWatermark() {
                return null;
            }

            @Override
            public long extractTimestamp(SensorReading sensorReading, long l) {
                return 0;
            }
        });


        env.execute(AverageSensorReadings.class.getSimpleName());

    }

    public static class SensorSource implements SourceFunction<SensorReading> {

        @Override
        public void run(SourceContext<SensorReading> sourceContext) throws Exception {

        }

        @Override
        public void cancel() {

        }
    }

    public static class TemperatureAverager implements WindowFunction<SensorReading, SensorReading, String, TimeWindow> {

        @Override
        public void apply(String s, TimeWindow timeWindow, Iterable<SensorReading> iterable, Collector<SensorReading> collector) throws Exception {

        }
    }

    //-------------------------练习 自定义各种Function

    /**
     * 1、MapFunction<类型，输出> 就是把输入类型转换格式，然后输出
     * 实现一个自定义Map函数：输入类型是对象 SensorReading，输出是字符串
     */
    static class MyMapFunction implements MapFunction<SensorReading, String> {

        @Override
        public String map(SensorReading sensorReading) throws Exception {
            return sensorReading.id;
        }
    }

    /**
     * 2、FilterFunction<类型>  做过滤
     */
    static class MyFilterFunction implements FilterFunction<SensorReading> {

        @Override
        public boolean filter(SensorReading sensorReading) throws Exception {
            return sensorReading.temperature >= 25;
        }
    }

    /**
     * 3、FlatMapFunction<类型, 输出> 拆分出更多的数据
     * 比如单词拆分：把每行句子，按空格拆分出单词
     * 然后输出 (单词, 1) 这样的格式
     */
    static class MyFlatMapFunction implements FlatMapFunction<String, Tuple2<String, Integer>> {

        @Override
        public void flatMap(String s, Collector<Tuple2<String, Integer>> collector) throws Exception {
            String[] fields = s.split(" ");
            for (String word : fields) {
                collector.collect(Tuple2.of(word, 1));
            }
        }
    }

    /**
     * 4、KeySelector<类型，key> 对输入类型，指定某个字段作为key
     */
    static class MyKeySelector implements KeySelector<SensorReading, String> {

        @Override
        public String getKey(SensorReading sensorReading) throws Exception {
            return sensorReading.id;
        }
    }

    /**
     * 5、ReduceFunction<类型> 最通用的聚合操作
     * 其他 sum() max() min() maxBy() minBy() 都实现自此
     * 这里做了一个比较，然后返回温度最高的对象
     */
    static class MyReduceFunction implements ReduceFunction<SensorReading> {

        @Override
        public SensorReading reduce(SensorReading t1, SensorReading t2) throws Exception {
            return t1.temperature >= t2.temperature ? t1 : t2;
        }
    }

    /**
     * RichFlatMapFunction 比 FlatMapFunction 多了 open
     */
    static class MyRichFlatMapFunction extends RichFlatMapFunction<SensorReading, String> {
        @Override
        public void open(Configuration parameters) throws Exception {

        }

        @Override
        public void flatMap(SensorReading sensorReading, Collector<String> collector) throws Exception {

        }
    }

    /**
     * 周期性的 发出 watermarks
     */
    static class MyAssignerWithPeriodicWatermarks implements AssignerWithPeriodicWatermarks<SensorReading> {
        Long currentMaxEventTime = 0L;      //当前窗口最大EventTime
        Long maxOutputOrderness = 10000L;   //最大乱序时间 10秒

        @Nullable
        @Override
        public Watermark getCurrentWatermark() {
            //当前窗口最大EventTime 向前延长 10秒
            // 比如当前EventTime=01:40:22
            // 那么   WaterMark=01:40:12
            return new Watermark(currentMaxEventTime - maxOutputOrderness);
        }

        @Override
        public long extractTimestamp(SensorReading sensorReading, long l) {
            currentMaxEventTime = Math.max(sensorReading.timestamp, currentMaxEventTime);
            //如果 sensorReading.timestamp 是毫秒，则 直接返回；
            //如果 sensorReading.timestamp 是秒，则这里要 *1000 再返回。
            //因为 watermark需要精确到毫秒
            Long timestamp = sensorReading.timestamp * 1000;
            return timestamp;
        }
    }

    static class MyAssignerWithPunctuatedWatermarks implements AssignerWithPunctuatedWatermarks<SensorReading> {

        @Nullable
        @Override
        public Watermark checkAndGetNextWatermark(SensorReading sensorReading, long l) {
            return null;
        }

        @Override
        public long extractTimestamp(SensorReading sensorReading, long l) {
            return 0;
        }
    }

}
