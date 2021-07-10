package org.lixl.opensource.kafka;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;

import java.util.*;
import java.util.regex.Pattern;

public class TestConsumer {

    private static Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();
    private static int count = 0;

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "bigdata03:9092,bigdata04:9092");
        props.put("group.id", "CountryCounter");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        //订阅主题
        consumer.subscribe(Collections.singletonList("CustomerCountry"));
        //订阅多个主题
        consumer.subscribe(Pattern.compile("test.*"), new ConsumerRebalanceListener() {

            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> collection) {

            }

            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> collection) {

            }
        });

        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println(String.format("topic=%s, partition=%s, offset=%d, customer=%s, country=%s\n",
                            record.topic(), record.partition(), record.offset(), record.key(), record.value()));
                    int updatedCount = 1;

                    //程序控制 提交偏移量
                    try {
                        //同步提交————broker对提交做出响应前会一直阻塞
                        consumer.commitSync();
                        //异步提交
                        consumer.commitAsync(new OffsetCommitCallback() {
                            @Override
                            public void onComplete(Map<TopicPartition, OffsetAndMetadata> map, Exception e) {
                                if (e != null) {
                                    //记录错误日志
                                }
                            }
                        });
                    } catch (CommitFailedException e) {
                        e.printStackTrace();
                        //没有提交成功，记录到错误日志中
                    }

                    //不基于 poll的提交偏移量
                    currentOffsets.put(
                            new TopicPartition(record.topic(), record.partition()),
                            new OffsetAndMetadata(record.offset() + 1, "no metadata"));
                    if (count % 1000 == 0) {
                        consumer.commitAsync(currentOffsets, null);
                        count++;
                    }
                }
            }
        } finally {
            consumer.close();
        }
    }
}
