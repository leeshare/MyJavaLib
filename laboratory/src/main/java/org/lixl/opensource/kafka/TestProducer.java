package org.lixl.opensource.kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;

public class TestProducer {

    private static Properties kafkaProps = new Properties();

    public static void main(String[] args) {
        kafkaProps.put("bootstrap.servers", "bigdata03:9092,bigdata04:9092");
        kafkaProps.put("metadata.broker.list", "bigdata03:9092,bigdata04:9092");
        kafkaProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        kafkaProps.put("request.required.acks", "-1");

        KafkaProducer<String, String> producer = new KafkaProducer<>(kafkaProps);

        ProducerRecord<String, String> record = new ProducerRecord<>("CustomerCountry", "Precision Products", "France");
        ProducerRecord<String, String> record2 = new ProducerRecord<>("CustomerCountry", "Biomedical Materials", "USA");
        //不指定key，并且使用默认分区器，记录将被随机发到主题内的各个分区上，轮询算法将消息均衡分布到各个分区上。
        ProducerRecord<String, String> record3 = new ProducerRecord<>("CustomerCountry", "USA");
        try {
            //同步发送
            producer.send(record).get();
            //异步发送
            producer.send(record2, new ProducerCallback());

            producer.send(record3, new ProducerCallback());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static class ProducerCallback implements Callback {

        @Override
        public void onCompletion(RecordMetadata recordMetadata, Exception e) {
            if (e != null) {
                e.printStackTrace();
            }
        }

    }
}
