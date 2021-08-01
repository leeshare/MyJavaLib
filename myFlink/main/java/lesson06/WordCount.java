package lesson06;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * 针对添加的包 flink-runtime-web_2.11
 * 我们可以通过  localhos:8081 来看job运行情况
 *      要想使用 web查看，需要将运行环境改成 createLocalEnvironmentWithWebUI
 *
 *      默认不设置并行度时，cpu有几个核，并行度就是几
 */
public class WordCount {
    public static void main(String[] args) throws Exception {
        //步骤一，获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(new Configuration());
        env.setParallelism(2);
        //步骤二，获取数据源
        DataStreamSource<String> data = env.socketTextStream("192.168.123.153", 9999);
        //步骤三：数据处理
        SingleOutputStreamOperator<Tuple2<String, Integer>> result = data.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(String s, Collector<Tuple2<String, Integer>> collector) throws Exception {
                String[] fields = s.split(",");
                for (String word : fields) {
                    collector.collect(new Tuple2<>(word, 1));
                }
            }
        })
                .keyBy(0)
                .sum(1);
        //步骤四：数据输出
        result.print().setParallelism(3);
        //步骤五：启动任务
        env.execute("word count");
    }
}
