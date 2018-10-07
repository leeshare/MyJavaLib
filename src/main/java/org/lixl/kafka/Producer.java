package org.lixl.kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * Created by lxl on 18/10/7.
 */
public class Producer {

    private KafkaProducer producer;
    private Properties kafkaProps = new Properties();
    public Producer(){
        kafkaProps.put("bootstrap.servers", "broker1:9092,broker2:9092");
        kafkaProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        producer = new KafkaProducer<String, String>(kafkaProps);
    }

    public void send(){
        //不指定键,则此消息会被随机的发送到主题内各个可用的分区上(通过默认分区器)
        //ProducerRecord<String, String> record =  new ProducerRecord<String, String>("ConsumerCountry", "Precision Products");
        //第三个参数就是 键(拥有相同的键会被写到同一分区)
        ProducerRecord<String, String> record =  new ProducerRecord<String, String>("ConsumerCountry", "Precision Products", "France");
        try {
            //1. 发完不做任何处理 不管其成功与失败
            //producer.send(record);
            //2. 同步发送消息到kafka
            //producer.send(record).get();
            //3. 异步发送消息
            producer.send(record, new DemoCallback());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
