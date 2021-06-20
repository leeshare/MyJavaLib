package org.lixl.opensource.flink;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class MapAndFilter {
    public static void main(String[] args) throws Exception {
        //一、获取环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //二、数据源
        DataStreamSource<Long> numberStream = env.addSource(new MyNoParalleSource());//.setParallelism(1);
        //三、处理数据
        SingleOutputStreamOperator<Long> dataStream = numberStream.map(new MapFunction<Long, Long>() {
            @Override
            public Long map(Long value) throws Exception {
                System.out.println("接收到了数据：" + value);
                return value;
            }
        });
        SingleOutputStreamOperator<Long> filterStream = dataStream.filter(new FilterFunction<Long>() {
            @Override
            public boolean filter(Long value) throws Exception {
                return value % 2 == 0;
            }
        });
        //四、结果处理
        filterStream.print().setParallelism(4);
        //五、执行
        env.execute("MapAndFilter");
    }
}
