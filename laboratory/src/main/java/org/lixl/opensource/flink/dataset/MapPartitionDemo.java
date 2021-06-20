package org.lixl.opensource.flink.dataset;

import org.apache.flink.api.common.functions.MapPartitionFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.operators.MapPartitionOperator;
import org.apache.flink.util.Collector;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Flink Batch操作（非Streaming）
 *
 * MapPartition 是针对RDD中每个分区的迭代器进行操作
 * Map          是针对RDD中每个元素进行操作
 */
public class MapPartitionDemo {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        ArrayList<String> data = new ArrayList<>();
        data.add("you jump");
        data.add("i jump");
        //
        DataSource<String> text = env.fromCollection(data);
        MapPartitionOperator<String, String> mapPartitionData = text.mapPartition(new MapPartitionFunction<String, String>() {
            @Override
            public void mapPartition(Iterable<String> values, Collector<String> out) throws Exception {
                Iterator<String> iterator = values.iterator();
                while (iterator.hasNext()) {
                    String next = iterator.next();
                    String[] split = next.split("\\W+");
                    for (String word : split) {
                        out.collect(word);
                    }
                }
            }
        });
        mapPartitionData.print();
    }
}
