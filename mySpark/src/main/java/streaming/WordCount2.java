package streaming;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class WordCount2 {
    public static void main(String[] args) throws Exception {
        SparkConf conf = new SparkConf();
        conf.setAppName("Java World Count");
        conf.setMaster("local[4]");

        String topics = "test";
        String groupId = "test_consumer";
        String brokers = "bigdata04:9092";
        HashSet<String> topicsSet = new HashSet<>(Arrays.asList(topics.split(",")));
        HashMap<Object, Object> kafkaParams = new HashMap<>();
        kafkaParams.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        kafkaParams.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

        JavaStreamingContext ssc = new JavaStreamingContext(conf, Durations.seconds(5));
        //步骤一：增加监听器，批次完成时自动帮你自动提交偏移量
        //ssc.addStreamingListener(new MyListener());
    }
}
