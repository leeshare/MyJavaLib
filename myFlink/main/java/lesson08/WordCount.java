package lesson08;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import org.apache.flink.util.Collector;

import java.util.Arrays;
import java.util.Properties;

/**
 * Kafka数据源
 */
public class WordCount {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //设置并行度
        env.setParallelism(3);
        String topic = "leetest";
        Properties consumerProperties = new Properties();
        consumerProperties.setProperty("bootstrap.servers", "192.168.123.152:9092");
        consumerProperties.setProperty("group.id", "testConsumer");


        FlinkKafkaConsumer011<String> myConsumer =
                new FlinkKafkaConsumer011<>(topic, new SimpleStringSchema(), consumerProperties);

        //source 3 task
        DataStreamSource<String> data = env.addSource(myConsumer);//.setParallelism(3);

        //flatMap 2 task
        SingleOutputStreamOperator<Tuple2<String, Integer>> wordOneStream = data.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            public void flatMap(String line,
                                Collector<Tuple2<String, Integer>> out) throws Exception {
                String[] fields = line.split(",");
                for (String word : fields) {
                    out.collect(Tuple2.of(word, 1));
                }
            }
        });//.setParallelism(2);
        //data.flatMap((String r, Collector<Tuple2<String, Integer>> out) -> Arrays.stream(r.split("\\,")).forEach(x -> out.collect(Tuple2.of(x, 1))) );


        //keyBy sum  2 task
        SingleOutputStreamOperator<Tuple2<String, Integer>> result = wordOneStream.keyBy(0).sum(1).setParallelism(2);

        //map  2 task
        result.map(tuple -> tuple.toString()).setParallelism(2)
                .print();//.setParallelism(1); //sink 1 task

        env.execute("WordCount2");

    }
}
