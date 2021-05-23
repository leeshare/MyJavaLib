package com.lixl.stream.ad;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lixl.stream.entity.AdLog;
import com.lixl.stream.entity.AdServerLog;
import com.lixl.stream.entity.ProcessInfo;
import com.lixl.stream.utils.*;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.RichWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.kafka.clients.producer.Producer;
import redis.clients.jedis.Jedis;

import java.util.Iterator;

public class AdLogRetryWindowFunction extends RichWindowFunction<AdLog, AdLog, String, TimeWindow> {
    String tableName = Constants.TABLE_NAME;
    HTable hTable;
    Jedis jedis;
    Producer producer;
    ObjectMapper objectMapper;

    @Override
    public void open(Configuration parameters) throws Exception {
        hTable = HBaseUtils.initHBaseClient(tableName);
        jedis = RedisUtils.initRedis();
        producer = KafkaProducerUtils.getProducer();
        objectMapper = new ObjectMapper();
        super.open(parameters);
    }

    @Override
    public void apply(String requestId, TimeWindow timeWindow, Iterable<AdLog> iterable, Collector<AdLog> collector) throws Exception {
        Iterator<AdLog> itr = iterable.iterator();
        while(itr.hasNext()) {
            AdLog adLog = itr.next();
            //每次重试间隔大于1秒（避免1秒内重试了4次，就认为失败了）
            if(System.currentTimeMillis() - adLog.getProcessInfo().getProcessTimestamp() > 1000) {
                byte[] key = ETLUtils.generateBytesKey(adLog);
                AdServerLog context = ETLUtils.getContext(jedis, key);
                AdLog.Builder adLogBuilder = adLog.toBuilder();
                ProcessInfo.Builder processBuilder = adLogBuilder.getProcessInfo().toBuilder();
                processBuilder.setRetryCount(processBuilder.getRetryCount() + 1);
                processBuilder.setProcessTimestamp(System.currentTimeMillis());
                adLogBuilder.setProcessInfo(processBuilder.build());

                if(context == null && adLog.getProcessInfo().getRetryCount() < 5) {
                    //继续发送到kafka
                    ETLUtils.sendRetry(producer, adLogBuilder.build().toByteArray());
                } else {
                    //读到server_log 或 超过5次还未读到的，都发到下游去了，这里就不管了。
                    ETLUtils.joinContext(adLogBuilder, context);
                    collector.collect(adLog);
                }
            }
        }

    }
}
