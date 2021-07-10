package flink;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.ArrayList;

/**
 * 测试代码时 有用
 */
public class StreamingSourceFromCollection {
    public static void main(String[] args) throws Exception {
        //一、执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //二、设置数据源
        ArrayList<String> data = new ArrayList<>();
        data.add("hadoop");
        data.add("spark");
        data.add("flink");

        DataStreamSource<String> dataStream = env.fromCollection(data);
        //三、transformation操作
        SingleOutputStreamOperator<String> addPreStream = dataStream.map(new MapFunction<String, String>() {
            @Override
            public String map(String word) throws Exception {
                return "my_" + word;
            }
        });
        //四、结果处理
        addPreStream.print().setParallelism(1);
        //五、启动程序
        env.execute("StreamingSourceFromCollection");
    }
}
