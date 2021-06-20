package window;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

public class windowByType {
    public static void main(String[] args) {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        DataStreamSource<String> dataStream = env.socketTextStream("bigdata02", 8888);
        SingleOutputStreamOperator<Tuple2<String, Long>> stream = dataStream.map(new MapFunction<String, Tuple2<String, Long>>() {
            @Override
            public Tuple2<String, Long> map(String line) throws Exception {
                String[] fields = line.split(",");
                return new Tuple2<>(fields[0], Long.parseLong(fields[1]));
            }
        });

        stream.keyBy(0)
                .timeWindow(Time.minutes(1))
                .sum(1).print();
        stream.keyBy(0)
                .timeWindow(Time.seconds(10), Time.seconds(5))
                .sum(0).print();

        stream.keyBy(0)
                .countWindow(100)
                .sum(1).print();

        stream.keyBy(0)
                //.window(TumblingEventTimeWindows.of(Time.seconds(2)))
                .window(SlidingEventTimeWindows.of(Time.seconds(10), Time.seconds(5)))
                .sum(1)
                .print();
    }
}
