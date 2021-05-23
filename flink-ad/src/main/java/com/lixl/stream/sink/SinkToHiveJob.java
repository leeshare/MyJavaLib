package com.lixl.stream.sink;

import com.lixl.stream.entity.AdClientLog;
import com.lixl.stream.entity.AdLog;
import com.lixl.stream.entity.AdServerLog;
import com.lixl.stream.entity.schema.AdClientLogSchema;
import com.lixl.stream.entity.schema.AdServerLogSchema;
import com.lixl.stream.utils.Constants;
import com.lixl.stream.utils.FlinkKafkaConsumerUtils;
import com.twitter.chill.protobuf.ProtobufSerializer;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.fs.bucketing.Bucketer;
import org.apache.flink.streaming.connectors.fs.bucketing.BucketingSink;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;

import java.util.Properties;

/**
 * 把原始数据写入HDFS中
 *      原始数据分两条线：1条是进入实时分析查询
 *          第二条是进入HDFS，Hive
 */
public class SinkToHiveJob {
    private static String KAFKA_SERVER_LOG = Constants.SERVER_LOG;
    private static String KAFKA_CLIENT_LOG = Constants.CLIENT_LOG;
    private static String KAFKA_AD_LOG = Constants.AD_LOG;
    private static String BROKERS = Constants.BROKERS;
    //比如实际存储格式  hadoop fs -ls /home/json/server_log_v30/dt=20210501/hour=20
    private static String partition = "'dt='yyyyMMdd/'hour'=HH";
    private static String HDFS_LOG_HOME = Constants.HDFS_LOG_HOME;

    public static void config(BucketingSink sink) {
        sink.setUseTruncate(false);
        // 256M 一个文件
        sink.setBatchSize(1024 * 1024 * 256L);
        // 30分钟
        sink.setBatchRolloverInterval(30 * 60 * 1000L);
        // 3分钟不写入就从 in-progress 转变为 pending
        sink.setInactiveBucketCheckInterval(3 * 60 * 1000L);
        // 30秒检查一次 多久没有写入了，用于判断是否从 in-progress 转变为 pending
        sink.setInactiveBucketCheckInterval(30 * 1000L);

        sink.setInProgressSuffix(".in-progress");
        sink.setPendingSuffix(".pending");
    }



    /**
     * 把从flink数据，sink到指定的 filesystem 中
     *      我们是要 sink到 HDFS中，按日期进行归档
     *
     * 依赖于 fink-connector-filesystem
     * @param streamSource
     * @param topic
     * @param bucketer
     */
    public static void buildSink(DataStreamSource streamSource, String topic, Bucketer bucketer) {
        BucketingSink stringSink = new BucketingSink<>(HDFS_LOG_HOME + "json/" + topic);
        //提供了处理方式 通过此方式来实现：数据按事件时间分区
        stringSink.setBucketer(bucketer);
        // 具体处理：按照什么格式去处理：按照ProtoBuf转成Json格式去处理
        stringSink.setWriter(new ProtobufStringWriter<>());
        // 配置
        config(stringSink);
        //
        streamSource.addSink(stringSink).name(topic + "-JsonSink");
    }

    public static void main(String[] args) throws Exception {
        String groupId = "fink-sink-task-test";
        // set up the streaming excution environment
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.enableCheckpointing(60000);
        env.setParallelism(2);
        env.getCheckpointConfig().setFailOnCheckpointingErrors(false);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        env.getConfig().enableForceAvro();
        env.getConfig().registerTypeWithKryoSerializer(AdServerLog.class, ProtobufSerializer.class);
        env.getConfig().registerTypeWithKryoSerializer(AdClientLog.class, ProtobufSerializer.class);
        env.getConfig().registerTypeWithKryoSerializer(AdLog.class, ProtobufSerializer.class);
        Properties propertiesServer = FlinkKafkaConsumerUtils.getConsumerProperties(BROKERS, KAFKA_SERVER_LOG, groupId);
        Properties propertiesClient = FlinkKafkaConsumerUtils.getConsumerProperties(BROKERS, KAFKA_CLIENT_LOG, groupId);
        //读 server_log 和 client_log 数据源
        DataStreamSource<AdServerLog> adServerInputStream = env.addSource(
                new FlinkKafkaConsumer010<AdServerLog>(KAFKA_SERVER_LOG, new AdServerLogSchema(), propertiesServer)
        );
        DataStreamSource<AdClientLog> adClientInputStream = env.addSource(
                new FlinkKafkaConsumer010<AdClientLog>(KAFKA_CLIENT_LOG, new AdClientLogSchema(), propertiesClient)
        );

        buildSink(adServerInputStream, KAFKA_SERVER_LOG, new AdServerEventTimeBucketer(partition));
        buildSink(adClientInputStream, KAFKA_CLIENT_LOG, new AdClientEventTimeBucketer(partition));

        env.execute("Stream sink job");
    }
}
