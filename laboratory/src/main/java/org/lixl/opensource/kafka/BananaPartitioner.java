package org.lixl.opensource.kafka;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.record.InvalidRecordException;
import org.apache.kafka.common.utils.Utils;

import java.util.List;
import java.util.Map;

/**
 * 自定义一个分区器
 */
public class BananaPartitioner implements Partitioner {
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueByes, Cluster cluster) {
        List<PartitionInfo> partitionInfos = cluster.partitionsForTopic(topic);
        int numPartitions = partitionInfos.size();
        if ((keyBytes == null) || (!(key instanceof String))) {
            //不是字符串 就抛出异常
            throw new InvalidRecordException("We expect all messages to have consumer name as key");
        }
        //这里key应该通过 configure 方法传进来
        if (((String) key).equals("Banana")) {
            return numPartitions;   //Banana总是被分配到最后一个分区
        }
        return (Math.abs(Utils.murmur2(keyBytes)) % (numPartitions - 1));
        //return 0;
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
