package org.lixl.opensource.kafka;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.common.TopicPartition;

import java.util.Collection;

public class SaveOffsetsOnRebalance implements ConsumerRebalanceListener {
    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> collection) {

    }

    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> collection) {

    }
}
