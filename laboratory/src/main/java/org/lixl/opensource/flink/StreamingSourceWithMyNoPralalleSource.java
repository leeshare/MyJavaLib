package org.lixl.opensource.flink;

//import com.google.inject.internal.cglib.proxy.$FixedValue;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * 从自定义的数据源里面获取数据，然后过滤出偶数
 */
public class StreamingSourceWithMyNoPralalleSource {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //二、数据源
        DataStreamSource<Long> numberStream = env.addSource(new MyNoParalleSource()).setParallelism(1);
        //三、数据处理
        SingleOutputStreamOperator<Long> dataStream = numberStream.map(new MapFunction<Long, Long>() {
            @Override
            public Long map(Long aLong) throws Exception {
                System.out.println("接收到了数据：" + aLong);
                return aLong;
            }
        });
        SingleOutputStreamOperator<Long> filterDataStream = dataStream.filter(new FilterFunction<Long>() {
            @Override
            public boolean filter(Long number) throws Exception {
                return number % 2 == 0;
            }
        });
        //四、结果处理
        filterDataStream.print().setParallelism(1);
        //五、执行
        env.execute("StreamingWithMyNoPralalleSource");
    }
}
