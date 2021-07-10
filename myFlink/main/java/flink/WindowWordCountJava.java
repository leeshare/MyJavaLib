package flink;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

/**
 * 滑动窗口实现单词计数
 * 数据源：socket
 * 需求：每隔1秒计算最近2秒单词出现的次数
 * <p>
 * 练习算子：flatMap
 * keyBy：
 * dataStream.keyBy("someKey")
 * dataStream.keyBy(0)
 */
public class WindowWordCountJava {
    public static void main(String[] args) throws Exception {
        int port;

        try {
            ParameterTool parameterTool = ParameterTool.fromArgs(args);
            port = parameterTool.getInt("port");
        } catch (Exception e) {
            System.out.println("no port set, user default port 9988");
            port = 9988;
        }
        //一 获取运行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        String hostName = "192.168.123.152";
        String delimiter = "\n";
        //二、设置数据源
        DataStreamSource<String> textStream = env.socketTextStream(hostName, port, delimiter);
        //三、数据处理
        SingleOutputStreamOperator<WordCount> wordCountStream = textStream.flatMap(new FlatMapFunction<String, WordCount>() {
            @Override
            public void flatMap(String line, Collector<WordCount> out) throws Exception {
                String[] fields = line.split("\t");
                for (String word : fields) {
                    out.collect(new WordCount(word, 1L));
                }
            }
        });
        SingleOutputStreamOperator<WordCount> wordCountStream2 = wordCountStream.keyBy("word").timeWindow(Time.seconds(2), Time.seconds(1)).sum("count");
        //四、结果处理
        wordCountStream2.print().setParallelism(1);
        //五、运行
        env.execute("socket word count");
    }

    public static class WordCount {
        public String word;
        public long count;

        public WordCount() {

        }

        public WordCount(String word, long count) {
            this.word = word;
            this.count = count;
        }

        @Override
        public String toString() {
            return "WordCount{" +
                    "word='" + word + '\'' +
                    ", count=" + count +
                    '}';
        }
    }
}
