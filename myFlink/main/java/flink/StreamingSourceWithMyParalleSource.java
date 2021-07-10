package flink;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class StreamingSourceWithMyParalleSource {
    public static void main(String[] args) throws Exception {
        //一、获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //二、数据源
        DataStreamSource<Long> numberStream = env.addSource(new MyParallelSource()).setParallelism(2);
        //三、数据处理
        SingleOutputStreamOperator<Long> dataStream = numberStream.map(new MapFunction<Long, Long>() {
            @Override
            public Long map(Long value) throws Exception {
                System.out.println("接收到数据：" + value);
                return value;
            }
        });
        SingleOutputStreamOperator<Long> filterStream = dataStream.filter(new FilterFunction<Long>() {
            @Override
            public boolean filter(Long value) throws Exception {
                return value % 2 != 0;
            }
        });
        //四、结果处理
        filterStream.print().setParallelism(1);
        //五、运行
        env.execute("StreamingWithMyParallelSource");


    }
}
