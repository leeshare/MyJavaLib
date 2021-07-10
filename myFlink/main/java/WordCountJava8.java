import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

import java.util.Arrays;

public class WordCountJava8 {

    public static void main(String[] args) throws Exception {
        String path = "file:///C:/BigData/upload_data/str.data";

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        DataStreamSource<String> source = env.readTextFile(path);
        SingleOutputStreamOperator<Tuple2<String, Integer>> wordsAndOne = source.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(String s, Collector<Tuple2<String, Integer>> collector) throws Exception {
                String[] fields = s.split("\\|");
                for (String word : fields) {
                    collector.collect(new Tuple2<>(word, 1));
                }
            }
        });
        //data.flatMap((String r, Collector<Tuple2<String, Integer>> out) -> Arrays.stream(r.split("\\,")).forEach(x -> out.collect(Tuple2.of(x, 1))) );

        SingleOutputStreamOperator<Tuple2<String, Integer>> wordsAndCount = wordsAndOne.keyBy(0).sum(1);
        wordsAndCount.print();


        env.execute(WordCountJava8.class.getName());
    }
}
