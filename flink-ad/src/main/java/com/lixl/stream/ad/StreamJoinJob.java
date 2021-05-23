package com.lixl.stream.ad;

import com.lixl.stream.entity.AdClientLog;
import com.lixl.stream.entity.AdLog;
import com.lixl.stream.entity.AdServerLog;
import com.lixl.stream.entity.schema.AdClientLogSchema;
import com.lixl.stream.entity.schema.AdLogSchema;
import com.lixl.stream.entity.schema.AdServerLogSchema;
import com.lixl.stream.utils.Constants;
import com.lixl.stream.utils.FlinkKafkaConsumerUtils;
import com.lixl.stream.utils.FlinkKafkaProducerUtils;
import com.twitter.chill.protobuf.ProtobufSerializer;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumerBase;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer010;

import java.util.Properties;

public class StreamJoinJob {
    private static String KAFKA_SERVER_LOG = Constants.SERVER_LOG;
    private static String KAFKA_CLIENT_LOG = Constants.CLIENT_LOG;
    private static String KAFKA_AD_LOG = Constants.AD_LOG;
    private static String KAFKA_AD_LOG_REPORT = Constants.AD_LOG_REPORT;
    private static String BROKERS = Constants.BROKERS;

    /**
     * Flink做了这么几件事：
     *  1 把Kafka的server_log数据写入分级缓存 Redis和 HBase
     *  2 从分级缓存中读取出server_log数据
     *      2.2 做重试操作
     *  3 统计
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception{
        String groupId = "flink-join-test";
        //设置 streaming execution 环境
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //checkpoint = 1分钟
        env.enableCheckpointing(60000);
        //并行度 = 2
        env.setParallelism(2);
        env.getCheckpointConfig().setFailOnCheckpointingErrors(false);
        //时间戳 是 实现时间
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        env.getConfig().enableForceAvro();
        //注册了2个序列化工具类
        env.getConfig().registerTypeWithKryoSerializer(AdServerLog.class, ProtobufSerializer.class);
        env.getConfig().registerTypeWithKryoSerializer(AdLog.class, ProtobufSerializer.class);

        //生成2份 kafka 消费者的配置文件
        Properties propertiesServer = FlinkKafkaConsumerUtils.getConsumerProperties(BROKERS, KAFKA_SERVER_LOG, groupId);
        Properties propertiesClient = FlinkKafkaConsumerUtils.getConsumerProperties(BROKERS, KAFKA_CLIENT_LOG, groupId);
        //添加数据源：使用FlinkKafkaConsumer010来读
        //要解成 AdServerLog，就需要反序列化
        DataStreamSource<AdServerLog> adServerInputStream = env.addSource(new FlinkKafkaConsumer010<AdServerLog>(KAFKA_SERVER_LOG, new AdServerLogSchema(), propertiesServer));
        DataStreamSource<AdClientLog> adClientInputStream = env.addSource(new FlinkKafkaConsumer010<AdClientLog>(KAFKA_CLIENT_LOG, new AdClientLogSchema(), propertiesClient));

        //正常项目中，必须拆分成3个任务:写缓存任务、拼接任务、统计任务

        //job1 把数据写入kv 分级缓存中
        adServerInputStream.flatMap(new AdServerLogRichFlatMap()).name("WriteServerContext");

        //job2

        //client_log 从分级缓存中读取数据
        DataStream<AdLog> adLogDataStream = adClientInputStream.flatMap(new AdClientLogRichFlatMap());
        // 添加 sink
        Properties producerProperties = FlinkKafkaProducerUtils.getProducerProperties(BROKERS);
        FlinkKafkaProducer010<AdLog> adLogSink = new FlinkKafkaProducer010<AdLog>(KAFKA_AD_LOG, new AdLogSchema(), producerProperties);

        adLogDataStream.addSink(adLogSink).name("AdLogProcesser");

        //job3 统计服务
        FlinkKafkaProducer010<String> adLogReportSink = new FlinkKafkaProducer010<String>(KAFKA_AD_LOG_REPORT, new SimpleStringSchema(), producerProperties);
        adLogDataStream.flatMap(new AdLogRichFlatMap()).addSink(adLogReportSink).name("AdLogReport");

        env.execute("Stream join job");
    }
}
