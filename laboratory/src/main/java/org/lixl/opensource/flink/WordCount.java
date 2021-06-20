package org.lixl.opensource.flink;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

public class WordCount {

    public static void main(String[] args) throws Exception {
        //步骤一：获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //步骤二：数据的输入
        DataStreamSource<String> data = env.socketTextStream("192.168.123.152", 9999);
        //步骤三：数据的处理

        //1、最初的实现
        /*SingleOutputStreamOperator<Tuple2<String, Integer>> wordAndOne = data.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(String line, Collector<Tuple2<String, Integer>> out) throws Exception {
                String[] fields = line.split(",");
                for (String word : fields) {
                    out.collect(new Tuple2<>(word, 1));
                }
            }
        });
        SingleOutputStreamOperator<Tuple2<String, Integer>> result = wordAndOne.keyBy(0).sum(1);
        */

        //2、使用自定义输出
        /*SingleOutputStreamOperator<WordAndCount> wordAndOne = data.flatMap(new FlatMapFunction<String, WordAndCount>() {
            @Override
            public void flatMap(String line, Collector<WordAndCount> out) throws Exception {
                String[] field = line.split(",");
                for (String word : field) {
                    out.collect(new WordAndCount(word, 1));
                }
            }
        });
        */
        //3、最终 使用自定义算子，代码结构异常清晰简洁
        SingleOutputStreamOperator<WordAndCount> wordAndOne = data.flatMap(new SplitLine());
        SingleOutputStreamOperator<WordAndCount> result = wordAndOne.keyBy("word").sum("count");


        //步骤四：数据的输出
        result.print();
        //步骤五：启动任务
        env.execute("word count...");
    }

    /**
     * 自定义算子
     */
    public static class SplitLine implements FlatMapFunction<String, WordAndCount> {

        @Override
        public void flatMap(String line, Collector<WordAndCount> out) throws Exception {
            String[] fields = line.split(",");
            for(String word: fields) {
                out.collect(new WordAndCount(word, 1));
            }
        }
    }

    /**
     * 如果Tuple2返回的字段太多了（而不只是这里的<String, Integer>）
     * 则我们使用面向对象的方法去实现
     *      我们自定义一个对象
     */
    public static class WordAndCount {
        private String word;
        private int count;

        public WordAndCount(){

        }

        @Override
        public String toString() {
            return "WordAndCount{" +
                    "word='" + word + '\'' +
                    ", count=" + count +
                    '}';
        }

        public WordAndCount(String word, int count) {
            this.word = word;
            this.count = count;
        }

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
