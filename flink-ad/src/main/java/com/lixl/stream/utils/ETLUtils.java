package com.lixl.stream.utils;

import com.google.protobuf.InvalidProtocolBufferException;
import com.lixl.stream.entity.AdClientLog;
import com.lixl.stream.entity.AdLog;
import com.lixl.stream.entity.AdServerLog;
import com.lixl.stream.entity.ProcessInfo;
import com.lixl.stream.entity.dto.AdLogDTO;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import redis.clients.jedis.Jedis;

import java.io.IOException;

public class ETLUtils {

    static final String RETRY_TOPIC = Constants.CLIENT_LOG_RETRY;

    //当没有拿到 server_log，则重试：把key 重新打入join_kafka中
    public static void sendRetry(Producer producer, byte[] value) {
        sendKafka(producer, RETRY_TOPIC, value);
    }
    public static void sendKafka(Producer producer, String topic, byte[] value) {
        ProducerRecord<String, byte[]> record = new ProducerRecord<>(topic, value);
        producer.send(record);
    }
    //生成唯一key    client_log 通过这3个字段生成的 唯一key，再读出来
    public static byte[] generateBytesKey(AdClientLog context) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(context.getRequestId());
        stringBuilder.append(context.getCreativeId());
        stringBuilder.append(context.getUnitId());
        return stringBuilder.toString().getBytes();
    }
    //生成唯一key    server_log 使用这3个唯一字段生成 唯一key
    public static byte[] generateBytesKey(AdServerLog context) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(context.getRequestId());
        stringBuilder.append(context.getCreativeId());
        stringBuilder.append(context.getUnitId());
        return stringBuilder.toString().getBytes();
    }

    public static byte[] generateBytesKey(AdLog context) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(context.getRequestId());
        stringBuilder.append(context.getCreativeId());
        stringBuilder.append(context.getUnitId());
        return stringBuilder.toString().getBytes();
    }
    //要拼接的的信息
    public static AdServerLog generateContext(AdServerLog adServerLog) {
        AdServerLog log = AdServerLog.newBuilder()
                .setGender(adServerLog.getGender())
                .setAge(adServerLog.getAge())
                .setCountry(adServerLog.getCountry())
                .setSourceType(adServerLog.getSourceType())
                .setBidType(adServerLog.getBidType())
                .build();
        return log;
    }

    //只有 server log才写 redis
    public static void writeRedis(Jedis jedis, byte[] key, AdServerLog context) {
        jedis.set(key, context.toByteArray());
        jedis.expire(key, Constants.DEFAULT_EXPIRE);
    }

    static final byte[] DEFAULT_COLUMN_FAMILY = Bytes.toBytes("cf1");
    static final byte[] DEFAULT_COLUMN_KEY = Bytes.toBytes("v");
    //只有 server log才写 HBase
    public static void writeHBase(HTable hTable, byte[] key, AdServerLog context) {
        try {
            Put put = new Put(key);
            put.add(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN_KEY, context.toByteArray());
            hTable.put(put);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static AdServerLog getContext(Jedis jedis, byte[] key) {
        byte[] data = jedis.get(key);
        if(data == null)
            return null;
        try {
            return AdServerLog.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static AdServerLog getContext(HTable hTable, byte[] key) {
        Get get = new Get(key);
        try {
            Result data = hTable.get(get);
            if(data == null)
                return null;
            return AdServerLog.parseFrom(data.getValue(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN_KEY));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取 server log 记录
     *  先从 redis中取，没有的话再从HBase中取
     * @param jedis
     * @param hTable
     * @param key
     * @return
     */
    public static AdServerLog getContext(Jedis jedis, HTable hTable, byte[] key)  {
        AdServerLog context = getContext(jedis, key);
        if(context == null)
            context = getContext(hTable, key);
        return context;
    }

    /**
     * 把 ad_server_log 关联到 ad_client_log 上，
     * 从而生成 ad_log
     * @param adClientLog
     * @param context
     * @return
     */
    public static AdLog buildAdLog(AdClientLog adClientLog, AdServerLog context) {
        AdLog.Builder adLogBuilder = AdLog.newBuilder();
        ProcessInfo.Builder processInfoBuilder = ProcessInfo.newBuilder();
        processInfoBuilder.setProcessTimestamp(System.currentTimeMillis());

        adLogBuilder.setRequestId(adClientLog.getRequestId());
        adLogBuilder.setTimestamp(adClientLog.getTimestamp());
        adLogBuilder.setDeviceId(adClientLog.getDeviceId());
        adLogBuilder.setOs(adClientLog.getOs());
        adLogBuilder.setNetwork(adClientLog.getNetwork());
        adLogBuilder.setUserId(adClientLog.getUserId());
        if(context != null) {
            processInfoBuilder.setJoinServerLog(true);
            joinContext(adLogBuilder, context);
        } else {
            processInfoBuilder.setRetryCount(1);
            processInfoBuilder.setJoinServerLog(false);
        }
        adLogBuilder.setSourceType(adClientLog.getSourceType());
        adLogBuilder.setPosId(adClientLog.getPosId());
        adLogBuilder.setAccountId(adClientLog.getAccountId());
        adLogBuilder.setCreativeId(adClientLog.getCreativeId());
        adLogBuilder.setUnitId(adClientLog.getUnitId());
        switch (adClientLog.getEventType()) {
            case "SEND":
                adLogBuilder.setSend(1);
                break;
            case "IMPRESSION":
                adLogBuilder.setImpression(1);
                break;
            case "CLICK":
                adLogBuilder.setClick(1);
                break;
            case "DOWNLOAD":
                adLogBuilder.setDownload(1);
                break;
            case "INSTALLED":
                adLogBuilder.setInstalled(1);
                break;
            case "PAY":
                adLogBuilder.setPay(1);
                break;
            default:
                break;
        }
        return adLogBuilder.build();

    }

    //具体 把 ad_server_log 关联到 已经通过 ad_client_log 生成的 ad_log上。
    public static void joinContext(AdLog.Builder adLogBuilder, AdServerLog context) {
        adLogBuilder.setGender(context.getGender());
        adLogBuilder.setAccountId(context.getAge());
        adLogBuilder.setCountry(context.getCountry());
        adLogBuilder.setProvince(context.getProvince());
        adLogBuilder.setCity(context.getCity());

        adLogBuilder.setBidPrice(context.getBidPrice());
    }

    public static AdLogDTO buildAdLogDTO(AdLog adLog) {
        AdLogDTO adLogDTO = new AdLogDTO();
        adLogDTO.setRequestId(adLog.getRequestId());
        adLogDTO.setTimestamp(adLog.getTimestamp());
        adLogDTO.setDeviceId(adLog.getDeviceId());
        adLogDTO.setOs(adLog.getOs());
        adLogDTO.setNetwork(adLog.getNetwork());
        adLogDTO.setUserId(adLog.getUserId());
        adLogDTO.setGender(adLog.getGender());
        adLogDTO.setAge(adLog.getAge());
        adLogDTO.setCountry(adLog.getCountry());
        adLogDTO.setProvince(adLog.getProvince());
        adLogDTO.setCity(adLog.getCity());
        adLogDTO.setSourceType(adLog.getSourceType());
        adLogDTO.setBidType(adLog.getBidType());
        adLogDTO.setBidPrice(adLog.getBidPrice());
        adLogDTO.setPosId(adLog.getPosId());
        adLogDTO.setAccountId(adLog.getAccountId());
        adLogDTO.setCreativeId(adLog.getCreativeId());
        adLogDTO.setUnitId(adLog.getUnitId());
        adLogDTO.setEventType(adLog.getEventType());
        adLogDTO.setSend(adLog.getSend());
        adLogDTO.setImpression(adLog.getImpression());
        adLogDTO.setClick(adLog.getClick());
        adLogDTO.setDownload(adLog.getDownload());
        adLogDTO.setInstalled(adLog.getInstalled());
        adLogDTO.setPay(adLog.getPay());
        return adLogDTO;
    }

}
