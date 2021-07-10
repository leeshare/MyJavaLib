import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.operators.Order;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.*;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

import java.util.Arrays;

/**
 * Flink批处理实现Word count。  str.data
 */
public class WordCount {

    public static void main(String[] args) throws Exception {
        String path = WordCount.class.getClassLoader().getResource("str.data").getPath();
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        DataSource<String> dataSource = env.readTextFile(path);
        FlatMapOperator<String, String> flatMapSource = dataSource.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public void flatMap(String s, Collector<String> collector) throws Exception {
                String[] words = s.split("\\|");
                for (String word : words) {
                    collector.collect(word);
                }
            }
        });
        MapOperator<String, Tuple2<String, Integer>> map = flatMapSource.map(new MapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public Tuple2<String, Integer> map(String s) throws Exception {
                return new Tuple2<>(s, 1);
            }
        });
        UnsortedGrouping<Tuple2<String, Integer>> groupBy = map.groupBy(0);
        AggregateOperator<Tuple2<String, Integer>> result = groupBy.sum(1).setParallelism(1);
        SortPartitionOperator<Tuple2<String, Integer>> sortedResult = result.sortPartition(1, Order.DESCENDING);
        sortedResult.print();
        env.execute();
    }
}
