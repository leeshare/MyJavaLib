package org.lixl.opensource.flink;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;

/**
 * 多个流合并成一个流
 * union的一个限制：所有合并的流类型必须一致
 */
public class UnionDemo {
    public static void main(String[] args) throws Exception {
        //
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //
        DataStreamSource<Long> text1 = env.addSource(new MyNoParalleSource());
        DataStreamSource<Long> text2 = env.addSource(new MyNoParalleSource()).setParallelism(1);
        //
        DataStream<Long> text = text1.union(text2);
        SingleOutputStreamOperator<Long> num = text.map(new MapFunction<Long, Long>() {
            @Override
            public Long map(Long value) throws Exception {
                System.out.println("原始数据：" + value);
                return value;
            }
        });
        //每隔2秒处理一次数据
        SingleOutputStreamOperator<Long> sum = num.timeWindowAll(Time.seconds(2)).sum(0);
        //
        sum.print().setParallelism(1);
        //
        String jobName = UnionDemo.class.getSimpleName();
        env.execute(jobName);
    }
}
