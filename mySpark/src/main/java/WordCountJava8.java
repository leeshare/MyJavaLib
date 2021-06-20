import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;

public class WordCountJava8 {
    public static void main(String[] args) {
        //static String str = "file:///C://BigData/upload_data/str.data";
        String str = WordCountJava8.class.getClassLoader().getResource("str.data").getPath();

        SparkConf conf = new SparkConf().setMaster("local").setAppName("word count");
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaRDD<String> linesRDD = sc.textFile(str, 1);

        JavaRDD<String> wordsRDD = linesRDD.flatMap(l -> {
            String[] words = l.split("\\|");
            return Arrays.asList(words).iterator();
        });
        JavaPairRDD<String, Integer> pairRDD = wordsRDD.mapToPair(w -> new Tuple2<>(w, 1));
        JavaPairRDD<String, Integer> wordsAndCount = pairRDD.reduceByKey((p1, p2) -> p1 + p2);
        System.out.println("====================正常输出====================");
        wordsAndCount.foreach(w -> System.out.println(w._1() + " \t" + w._2()));

        JavaPairRDD<Integer, String> wordsAndCountSort = wordsAndCount.mapToPair(w -> w.swap());
        wordsAndCountSort = wordsAndCountSort.sortByKey(false);
        JavaPairRDD<String, Integer> pairRDDSort = wordsAndCountSort.mapToPair(w -> w.swap());
        System.out.println("====================排序后输出====================");
        pairRDDSort.foreach(w -> System.out.println(w._1() + " \t " + w._2()));
    }
}
