package window;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

/**
 * 通过定时器 和 state
 * 对需求：5秒后，不再输入单词时，就打印出该单词
 *
 * 完成了 SessionWindow的功能
 */
public class KeyedProcessFunctionWordCount {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        //非并行数据源
        DataStreamSource<String> dataStreamSource = env.socketTextStream("bigdata02", 9999);
        //Data Process
        DataStream<Tuple2<String, Integer>> wordOnes = dataStreamSource.flatMap(new WordOneFlatMapFunction());
        //keyed stream
        KeyedStream<Tuple2<String, Integer>, Tuple> wordGroup = wordOnes.keyBy(0);

        wordGroup.process(new CountWithTimeoutFunction()).print();

        env.execute("KeyedProcessFunctionWordCount");



    }


    //单词计数
    private static class WordOneFlatMapFunction implements FlatMapFunction<String, Tuple2<String, Integer>> {

        @Override
        public void flatMap(String line, Collector<Tuple2<String, Integer>> out) throws Exception {
            String[] words = line.toLowerCase().split(" ");
            for(String word : words) {
                Tuple2<String, Integer> wordOne = new Tuple2<>(word, 1);
                //将单词计数 1 的二元组输出
                out.collect(wordOne);
            }
        }
    }


    /**
     * 继承 KeyedProcessFunction  是因为 上文通过 keyBy
     *      如果不经过 keyBy，就使用 ProcessFunction
     * key的数据类型， 输入的数据类型，输出的数据类型
     */
    private static class CountWithTimeoutFunction extends KeyedProcessFunction<Tuple, Tuple2<String, Integer>, Tuple2<String, Integer>> {
        private ValueState<CountWithTimestamp> state;

        @Override
        public void open(Configuration parameters) throws Exception {
            state = getRuntimeContext()
                    .getState(new ValueStateDescriptor<CountWithTimestamp>("myState", CountWithTimestamp.class));
            //super.open(parameters);
        }

        @Override
        public void processElement(Tuple2<String, Integer> element, Context ctx, Collector<Tuple2<String, Integer>> out) throws Exception {
            //拿到当前key 对应的 state
            CountWithTimestamp currentState = state.value();
            if(currentState == null) {
                currentState = new CountWithTimestamp();
                currentState.key = element.f0;
            }
            //更新这个key的 state
            currentState.count++;
            currentState.lastModified = ctx.timerService().currentProcessingTime();

            state.update(currentState);

            //注册一个定时器
            ctx.timerService().registerProcessingTimeTimer(currentState.lastModified + 5000);
        }

        @Override
        public void onTimer(long timestamp, OnTimerContext ctx, Collector<Tuple2<String, Integer>> out) throws Exception {
            CountWithTimestamp curr = state.value();
            if(timestamp == curr.lastModified + 5000) {
                out.collect(Tuple2.of(curr.key, curr.count));
                state.clear();
            }
            //super.onTimer(timestamp, ctx, out);
        }
    }

    /**
     * state中 存储的数据
     */
    private static class CountWithTimestamp {
        public String key;
        public int count;
        public long lastModified;
    }

}
