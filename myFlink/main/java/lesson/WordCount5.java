package lesson;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 *  Flink 一个最佳实践  ————工具类
 */
public class WordCount5 {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        String hostname = parameterTool.get("hostname");
        Integer port = parameterTool.getInt("port");
        DataStreamSource<String> data = env.socketTextStream(hostname, port);
        SingleOutputStreamOperator<WordAndCount> wordData = data.flatMap(new SplitLine());
        SingleOutputStreamOperator<WordAndCount> result = wordData.keyBy("word").sum("count");
        result.print();

        env.execute("word count");
    }

    private static class SplitLine implements FlatMapFunction<String, WordAndCount> {

        @Override
        public void flatMap(String s, Collector<WordAndCount> collector) throws Exception {
            String[] fields = s.split(",");
            for (String word : fields) {
                collector.collect(new WordAndCount(word, 1));
            }
        }
    }

    public static class WordAndCount {
        private String word;
        private Integer count;

        /**
         *  少了 这个默认构造函数，就会报下面的错误：
         *
         //Exception in thread "main" org.apache.flink.api.common.InvalidProgramException: This type (GenericType<lesson.WordCount2.WordAndCount>) cannot be used as key.
         */
        public WordAndCount(){

        }

        public WordAndCount(String word, Integer count) {
            this.word = word;
            this.count = count;
        }

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        @Override
        public String toString() {
            return "WordAndCount{" +
                    "word='" + word + '\'' +
                    ", count=" + count +
                    '}';
        }
    }
}
