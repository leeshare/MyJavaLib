import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Iterator;

/**
 * 1、获取编程入口                     环境对象 链接对象
 * 2、通过编程入口获取数据抽象           source 对象
 * 3、针对数据抽象对象执行各种计算     Action Transformation
 * 4、提交任务运行                     Submit
 * 5、输出结果                       Sink
 * 6、回收资源                       stop close
 */
public class WordCountJava7 {
    static String str = "file:///C://BigData/upload_data/str.data";
    public static void main(String[] args) throws Exception {

        //设置环境
        SparkConf conf = new SparkConf().setAppName("wordCount").setMaster("local");
        //设置上下文
        JavaSparkContext sc = new JavaSparkContext(conf);
        //读取数据
        JavaRDD<String> linesRDD = sc.textFile(str, 1);
        //获得单词RDD
        /**
         * 1、flatMap 当中的参数就是指定逻辑
         * 2、逻辑不是一个函数，而是一个匿名函数
         * 3、linesRDD 是一个数据集合
         * 4、把 linesRDD 中的每个元数据，拿出来作为参数，给 call方法执行一次
         * 5、每次调用得到的结果，就拼接成为一个新的集合
         */
        JavaRDD<String> wordsRDD = linesRDD.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public Iterator<String> call(String s) throws Exception {
                //注意： . 、 $、 | 和 * 等转义字符，必须得加 \\。
                //注意：多个分隔符，可以用 | 作为连字符。
                String[] fields = s.split("\\|");
                return Arrays.asList(fields).iterator();
            }
        });
        //获得每个单词一次的RDD
        JavaPairRDD<String, Integer> pairRDD = wordsRDD.mapToPair(new PairFunction<String, String, Integer>() {
            @Override
            public Tuple2<String, Integer> call(String s) throws Exception {
                return new Tuple2<>(s, 1);
            }
        });
        //获得每个单词，统计一下出现了多少次的RDD
        JavaPairRDD<String, Integer> wordAndCount = pairRDD.reduceByKey(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer integer, Integer integer2) throws Exception {
                return integer + integer2;
            }
        });

        System.out.println("====================正常输出====================");
        //这里就可以输出了
        wordAndCount.foreach(new VoidFunction<Tuple2<String, Integer>>() {
            @Override
            public void call(Tuple2<String, Integer> stringIntegerTuple2) throws Exception {
                System.out.println(stringIntegerTuple2._1() + "\t" + stringIntegerTuple2._2());
            }
        });

        //如果要进行排序
        JavaPairRDD<Integer, String> wordAndOneSort = wordAndCount.mapToPair(new PairFunction<Tuple2<String, Integer>, Integer, String>() {
            @Override
            public Tuple2<Integer, String> call(Tuple2<String, Integer> stringIntegerTuple2) throws Exception {
                return stringIntegerTuple2.swap();
            }
        });

        wordAndOneSort = wordAndOneSort.sortByKey(false);

        wordAndCount = wordAndOneSort.mapToPair(new PairFunction<Tuple2<Integer, String>, String, Integer>() {
            @Override
            public Tuple2<String, Integer> call(Tuple2<Integer, String> integerStringTuple2) throws Exception {
                return integerStringTuple2.swap();
            }
        });

        System.out.println("====================排序后的输出====================");

        wordAndCount.foreach(new VoidFunction<Tuple2<String, Integer>>() {
            @Override
            public void call(Tuple2<String, Integer> stringIntegerTuple2) throws Exception {
                System.out.println(stringIntegerTuple2._1() + "\t" + stringIntegerTuple2._2());
            }
        });

        sc.close();

    }
}
