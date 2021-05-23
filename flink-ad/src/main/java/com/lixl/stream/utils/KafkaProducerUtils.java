package com.lixl.stream.utils;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;

import java.util.Properties;

public class KafkaProducerUtils {
    static Producer<String, String> producer;

    public static void init() {
        Properties props = new Properties();
        //配置kafka端口
        props.put("metadata.broker.list", Constants.BROKERS);
        props.put("bootstrap.servers", Constants.BROKERS);
        //配置value的序列化类
        props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        //配置key的序列化类
        props.put("key.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");

        props.put("producer.type", "async");
        /**
         * request.required.acks
         * 0 ： 只要消息发出去，不管落盘与否，都认为消息已发成功。（会导致消息丢失）
         * 1 ： 只要Partition Leader收到消息，且落盘成功，不管其他Follower，就认为消息已发成功。（kafka默认方式）
         * -1 ： 必须Follower收到了消息，才算消费发送成功
         */
        props.put("request.required.acks", "-1");

        producer = new KafkaProducer<String, String>(props);
    }

    private static Producer instance = null;
    public static Producer getProducer() {
        //单例模式  （在并发时是有问题的，通过 synchronized改成线程安全）
        if(producer == null){
            synchronized (instance.getClass()) {
                init();
            }
        }
        return producer;
    }
}
