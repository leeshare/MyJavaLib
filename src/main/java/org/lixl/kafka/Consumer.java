package org.lixl.kafka;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.utils.CollectionUtils;

import java.util.*;

/**
 * Created by lxl on 18/10/7.
 */
public class Consumer {
    KafkaConsumer<String, String> consumer;
    Properties props = new Properties();
    public Consumer(){
        props.put("bootstrap.servers", "broker1:9092,borker2:9092");
        props.put("group.id", "CountryCounter");    //指定了消费者群组的名字
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumer = new KafkaConsumer<String, String>(props);
    }

    //注意:以下说的提交是 consumer.pull(100); 之后,提交偏移量

    //1 自动提交
    //将 auto.commit.offset 设为 true auto.commit.interval.ms 默认为5s
    public void get(){
        //subscribe()方法接受一个主题列表作为参数
        consumer.subscribe(Collections.singletonList("customerCountries"));
        //可以用正则表达式 来匹配多个主题
        consumer.subscribe(Collections.singletonList("test.*"));

        try {
            //轮询
            while(true){
                ConsumerRecords<String, String> records = consumer.poll(100); //100是超时时间,用于控制poll的注射器时间
                for(ConsumerRecord<String, String> record: records){

                    int updatedCount = 1;

                }
            }
        } finally {
            consumer.close();
        }
    }

    //2 手动提交(同步)
    public void get2(){
        while(true){
            ConsumerRecords<String, String> records = consumer.poll(100);
            for(ConsumerRecord<String, String> record: records){
                System.out.printf("topic = %s, partition = %s, offset = %d, customer = %s, country = %s\n",
                        record.topic(), (record), record.offset(), record.key(), record.value());
                try {
                    //将自动提交 auto.commit.offset 设为 false. 用 commitSync() 来手动提交偏移量.
                    consumer.commitSync();
                } catch(CommitFailedException e){
                    //log e
                }
            }
        }
    }

    //3 异步提交
    public void get3(){
        while(true){
            ConsumerRecords<String, String> records = consumer.poll(100);
            for(ConsumerRecord<String, String> record: records){
                System.out.printf("topic = %s, partition = %s, offset = %d, customer = %s, country = %s\n",
                        record.topic(), record.partition(), record.offset(), record.key(), record.value());

                consumer.commitAsync();

                consumer.commitAsync(new OffsetCommitCallback() {
                    @Override
                    public void onComplete(Map<TopicPartition, OffsetAndMetadata> map, Exception e) {
                        if(e != null){
                            //log error
                        }

                    }
                });
            }
        }
    }

    //4 同步和异步组合提交
    public void get4(){
        try{
            while(true) {
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("topic = %s, partition = %s, offset = %d, customer = %s, country = %s\n",
                            record.topic(), record.partition(), record.offset(), record.key(), record.value());
                }
                consumer.commitAsync();
            }
        } catch(Exception e){
            //log e
        } finally {
            try {
                consumer.commitSync();
            }catch(CommitFailedException e){
                //log e
            }
        }
    }

    //5 提交特定的偏移量

    //


    public void commitDBTransaction(){

    }
    public int getOffsetFromDB(TopicPartition partition){
        return 0;
    }

    //再均衡监听器
    /**
     * 消费者在退出和进行分区再均衡前,会做一些清理工作
     * 如 消费者失去对一个分区的所有权之前 提交最后一个已处理记录的偏移量,比如关闭文件句柄\数据库连接等
     */
    public class SaveOffsetsOnRebalace implements ConsumerRebalanceListener {

        //在再均衡开始之前和消费者停止读取消息之后被调用
        public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
            commitDBTransaction();
        }

        //在重新分配分区之后和消费者开始读取消息之前被调用
        public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
            for (TopicPartition partition : partitions) {
                consumer.seek(partition, getOffsetFromDB(partition));
            }
        }
    }

    //重特点偏移量处开始处理记录(比如从数据库总存储的偏移量)
    public void getFromDb(){
        List<String> topics = Collections.singletonList("consumerCountries");
        //consumer.subscribe(topics, new SaveOffsetsOnRebalace(consumer));
        //报错???
        consumer.poll(0);
        while(true){
            ConsumerRecords<String, String> records = consumer.poll(100);
            for(ConsumerRecord<String, String> record: records){
                //processRecord(record);
                //storeRecordInDB(record);
                //storeOffsetInDB(record.toString(), record.partition(), record.offset());
            }
            commitDBTransaction();
        }

    }
}
