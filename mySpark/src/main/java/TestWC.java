import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;

public class TestWC {
    public static void main(String[] args) {
        String path = TestWC.class.getClassLoader().getResource("str.data").getPath();

        SparkConf conf = new SparkConf().setMaster("local").setAppName("wc");
        JavaSparkContext context = new JavaSparkContext(conf);
        JavaRDD<String> lineRDD = context.textFile(path);
        JavaRDD<String> wordRDD = lineRDD.flatMap(a -> Arrays.stream(a.split("\\|")).iterator());
        JavaPairRDD<String, Integer> pairRDD = wordRDD.mapToPair(p -> Tuple2.apply(p, 1));
        JavaPairRDD<String, Integer> resultRDD = pairRDD.reduceByKey((p1, p2) -> p1 + p2);

        resultRDD.foreach(a -> System.out.println(a._1 + ", " + a._2));

        JavaPairRDD<Integer, String> revertRDD = resultRDD.mapToPair(a -> a.swap());
        revertRDD = revertRDD.sortByKey(false);
        JavaPairRDD<String, Integer> resultRDD2 = revertRDD.mapToPair(a -> a.swap());
        resultRDD2.foreach(a -> System.out.println(a._1 + ", " + a._2));

    }
}
