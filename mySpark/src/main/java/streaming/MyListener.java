package streaming;

import javafx.css.PseudoClass;
import org.apache.spark.streaming.kafka.KafkaCluster;
import org.apache.spark.streaming.scheduler.OutputOperationInfo;
import org.apache.spark.streaming.scheduler.StreamingListener;
import org.apache.spark.streaming.scheduler.StreamingListenerBatchCompleted;
import scala.collection.JavaConversions;

import java.util.Map;

public abstract class MyListener implements StreamingListener {
    //kafka0.8 才有
    private KafkaCluster kc;
    public scala.collection.immutable.Map<String, String> kafkaParams;

    public MyListener(scala.collection.immutable.Map<String, String> kafkaParams){
        this.kafkaParams = kafkaParams;
        kc = new KafkaCluster(kafkaParams);
    }

    @Override
    public void onBatchCompleted(StreamingListenerBatchCompleted batchCompleted) {
        //super.onBatchCompleted(batchCompleted);
        scala.collection.immutable.Map<Object, OutputOperationInfo> opsMap = batchCompleted.batchInfo().outputOperationInfos();
        Map<Object, OutputOperationInfo> javaOpsMap = JavaConversions.mapAsJavaMap(opsMap);
        for(Map.Entry<Object, OutputOperationInfo> entry: javaOpsMap.entrySet()) {
            if(!"None".equalsIgnoreCase(entry.getValue().failureReason().toString())) {
                return;
            }
        }
    }
}
