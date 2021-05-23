package com.lixl.stream.ad;

import com.lixl.stream.entity.AdLog;
import com.lixl.stream.entity.schema.AdLogSchema;
import com.lixl.stream.utils.Constants;
import com.lixl.stream.utils.FlinkKafkaConsumerUtils;
import com.lixl.stream.utils.FlinkKafkaProducerUtils;
import com.twitter.chill.protobuf.ProtobufSerializer;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.triggers.ProcessingTimeTrigger;
import org.apache.flink.streaming.api.windowing.triggers.PurgingTrigger;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer010;

import java.util.Properties;

public class StreamJoinRetryJob {
    private static String KAFKA_CLIENT_LOG_RETRY = Constants.CLIENT_LOG;
    private static String KAFKA_AD_LOG = Constants.AD_LOG;
    private static String KAFKA_AD_LOG_REPORT = Constants.AD_LOG_REPORT;
    private static String BROKERS = Constants.BROKERS;

    public static void main(String[] args) throws Exception {
        String groupId = "flink-join-retry-test";
        // set up the streaming execution environment
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.enableCheckpointing(60000);
        env.getCheckpointConfig().setFailOnCheckpointingErrors(false);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        env.getConfig().enableForceAvro();
        env.getConfig().registerTypeWithKryoSerializer(AdLog.class, ProtobufSerializer.class);
        Properties properties = FlinkKafkaConsumerUtils.getConsumerProperties(BROKERS, KAFKA_CLIENT_LOG_RETRY, groupId);

        //把 ad_log读进来
        DataStreamSource<AdLog> adLogInputStream = env.addSource(
                new FlinkKafkaConsumer010<>(KAFKA_CLIENT_LOG_RETRY, new AdLogSchema(), properties));

        //重试逻辑
        //使用了 窗口的方法：把数据积累到2秒钟（client_log等 server_log等2秒）后，执行 retry
        DataStream<AdLog> adLogStream = adLogInputStream.keyBy(AdLog::getRequestId)
                .timeWindow(Time.seconds(2))
                .trigger(PurgingTrigger.of(ProcessingTimeTrigger.create()))
                .apply(new AdLogRetryWindowFunction());

        //sink到下游

        Properties producerProperties = FlinkKafkaProducerUtils.getProducerProperties(BROKERS);
        FlinkKafkaProducer010<AdLog> adLogSink = new FlinkKafkaProducer010<AdLog>(KAFKA_AD_LOG, new AdLogSchema(), producerProperties);
        adLogStream.addSink(adLogSink).name("Retry process.");
//        FlinkKafkaProducer010<String> adLogReportSink = new FlinkKafkaProducer010<String>(KAFKA_AD_LOG_REPORT, new SimpleStringSchema(), producerProperties);
//        adLogStream.flatMap(new AdLogRichFlatMap()).addSink(adLogReportSink).name("Retry to report");
        // execute program
        env.execute("Stream join retry job");
    }
}
