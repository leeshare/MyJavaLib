package org.lixl.opensource.flink;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.ConnectedStreams;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoMapFunction;

/**
 * Connect 和 union类似，但是：只能连接2个流，且两个流的类型可以不一致
 */
public class ConnectionDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env =
                StreamExecutionEnvironment.getExecutionEnvironment();
//获取数据源
        DataStreamSource<Long> text1 = env.addSource(new
                MyNoParalleSource()).setParallelism(1);//注意：针对此source，并行度只能设置为1
        DataStreamSource<Long> text2 = env.addSource(new
                MyNoParalleSource()).setParallelism(1);
        SingleOutputStreamOperator<String> text2_str = text2.map(new MapFunction<Long, String>() {
            @Override
            public String map(Long value) throws Exception {
                return "str_" + value;
            }
        });
        ConnectedStreams<Long, String> connectStream = text1.connect(text2_str);
        SingleOutputStreamOperator<Object> result = connectStream.map(new
            CoMapFunction<Long, String, Object>() {
                @Override
                public Object map1(Long value) throws Exception {
                    return value;
                }
                @Override
                public Object map2(String value) throws Exception {
                    return value;
                }
            });
        //打印结果
        result.print().setParallelism(1);
        String jobName = ConnectionDemo.class.getSimpleName();
        env.execute(jobName);
    }
}
