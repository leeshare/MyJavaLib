package streaming;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.Tuple2;

import java.util.Iterator;

public class WordCount {
    public static void main(String[] args) throws Exception{
        SparkConf conf = new SparkConf();
        conf.setMaster("local[1]");
        conf.setAppName("WordCount");

        JavaStreamingContext ssc = new JavaStreamingContext(conf, Durations.seconds(2));
        JavaReceiverInputDStream<String> myDataDStream = ssc.socketTextStream("bigdata02", 8888);
        JavaDStream<String> errorLines = myDataDStream.filter(new Function<String, Boolean>() {
            @Override
            public Boolean call(String s) throws Exception {
                return s.contains("error");
            }
        });
        errorLines.print();

        ssc.start();
        ssc.awaitTermination();

    }
}
