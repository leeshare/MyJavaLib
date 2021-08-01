package lesson;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * 使用面向对象的思路 来输出。
 *      因为 Tuple2 结构比较简单，只能输出 key value，无法输出比较复杂的结构
 * 把算子抽出来，单独定义，从而使主代码流程 清洗很多。
 */
public class WordCount4 {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> data = env.socketTextStream("192.168.123.153", 9988);
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
