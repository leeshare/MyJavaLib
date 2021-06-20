package org.lixl.opensource.flink;

import org.apache.flink.streaming.api.collector.selector.OutputSelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SplitStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.ArrayList;

/**
 * 根据规则把一个数据流切分成多个流
 * 应用场景
 *  可能实际工作中，数据流中混合了多种类似的数据，多种类型的数据处理规则不一样，
 *  所以就可以再根据一定的规则，
 *      把一个数据流切分成多个数据流，这样每个数据流就可以使用不同的处理逻辑了
 */
public class SplitDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //
        DataStreamSource<Long> numStream = env.addSource(new MyNoParalleSource()).setParallelism(1);
        //
        SplitStream<Long> splitStream = numStream.split(new OutputSelector<Long>() {
            @Override
            public Iterable<String> select(Long value) {
                ArrayList<String> outPut = new ArrayList<>();
                if (value % 2 == 0)
                    outPut.add("even");
                else
                    outPut.add("odd");
                return outPut;
            }
        });
        DataStream<Long> evenStream = splitStream.select("even");
        DataStream<Long> oddStream = splitStream.select("odd");
        DataStream<Long> moreStream = splitStream.select("odd", "even");
        //
        evenStream.print().setParallelism(1);
        String jobName = SplitDemo.class.getSimpleName();
        //
        env.execute(jobName);
    }
}
