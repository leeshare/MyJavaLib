package com.lixl.stream.mock;

import com.lixl.stream.entity.AdClientLog;
import com.lixl.stream.entity.AdServerLog;
import com.lixl.stream.utils.Constants;
import com.lixl.stream.utils.ETLUtils;
import com.lixl.stream.utils.KafkaProducerUtils;
import com.lixl.stream.utils.MockDataUtils;
import org.apache.kafka.clients.producer.Producer;

import java.util.Random;

public class MockData {
    Producer producer = KafkaProducerUtils.getProducer();
    int magicNum = 30;

    public void mockServerLog(String requestId, String deviceId, String os, String network, String userId, String sourceType,
                              String bidType, long posId, long accountId, long unitId, long creativeId, String eventType) {
        //Proto自动生成的类 使用了 建造者(Builder)模式
        AdServerLog serverLog = AdServerLog.newBuilder()
                .setRequestId(requestId)
                .setTimestamp(System.currentTimeMillis())
                .setDeviceId(deviceId)
                .setOs(os)
                .setNetwork(network)
                .setUserId(userId)
                .setSourceType(sourceType)
                .setBidType(bidType)
                .setPosId(posId)
                .setAccountId(accountId)
                .setUnitId(unitId)
                .setCreativeId(creativeId)
                .setEventType(eventType)

                .setGender(MockDataUtils.gender())
                .setAge(MockDataUtils.age())
                .setCountry(MockDataUtils.country())
                .setProvince(MockDataUtils.province())
                .setBidPrice(MockDataUtils.bidPrice())

                .build();
        ETLUtils.sendKafka(producer, Constants.SERVER_LOG, serverLog.toByteArray());
    }

    public void mockClientLog(String requestId, String deviceId, String os, String network, String userId, String sourceType,
                              String bidType, long posId, long accountId, long unitId, long creativeId, String eventType ) {
        AdClientLog clientLog = AdClientLog.newBuilder()
                .setRequestId(requestId)
                .setTimestamp(System.currentTimeMillis())
                .setDeviceId(deviceId)
                .setOs(os)
                .setNetwork(network)
                .setUserId(userId)
                .setSourceType(sourceType)
                .setBidType(bidType)
                .setPosId(posId)
                .setAccountId(accountId)
                .setUnitId(unitId)
                .setCreativeId(creativeId)
                .setEventType(eventType)
                .build();
        ETLUtils.sendKafka(producer, Constants.CLIENT_LOG, clientLog.toByteArray());
    }


    public void mock() {
        String requestId = MockDataUtils.requestId();
        String deviceId = MockDataUtils.deviceId();
        String os = MockDataUtils.os();
        String network = MockDataUtils.network();
        String userId = MockDataUtils.userId();
        String sourceType = MockDataUtils.sourceType();
        String bidType = MockDataUtils.bidType();
        long posId = MockDataUtils.posId();
        long accountId = MockDataUtils.accountId();
        long unitId = MockDataUtils.unitId(accountId);
        long creativeId = MockDataUtils.creativeId(unitId);

        //server_log 发送 SEND
        mockServerLog(requestId, deviceId, os, network, userId, sourceType, bidType, posId, accountId, unitId, creativeId, "SEND");
        //广告发送出后，如果在client曝光了
        if (isImpression(creativeId)) {
            //System.out.println("mock impression");
            //client_log 发送 曝光
            mockClientLog(requestId, deviceId, os, network, userId, sourceType, bidType, posId, accountId, unitId, creativeId, "IMPRESSION");
            //曝光后，如果点击了
            if (isClick(creativeId)) {
                //System.out.println("mock click");
                mockClientLog(requestId, deviceId, os, network, userId, sourceType, bidType, posId, accountId, unitId, creativeId, "CLICK");
                //点击后，如果下载了
                if (isDownload(creativeId)) {
                    //System.out.println("mock download");
                    mockClientLog(requestId, deviceId, os, network, userId, sourceType, bidType, posId, accountId, unitId, creativeId, "DOWNLOAD");
                    //下载后，如果安装了
                    if (isinstalled(creativeId)) {
                        //System.out.println("mock installed");
                        mockClientLog(requestId, deviceId, os, network, userId, sourceType, bidType, posId, accountId, unitId, creativeId, "INSTALLED");
                        //安装后，如果支付了
                        if (isPayed(creativeId)) {
                            System.out.println("mock pay");
                            mockClientLog(requestId, deviceId, os, network, userId, sourceType, bidType, posId, accountId, unitId, creativeId, "PAY");
                        }
                    }
                }
            }
        }
    }

    public boolean isImpression(long creativeId) {
        int base = new Random(creativeId).nextInt(10);
        return new Random().nextInt(37 + base) < base + magicNum;
    }

    public boolean isClick(long creativeId) {
        int base = new Random(creativeId).nextInt(10);
        return new Random().nextInt(47 + base) < base + magicNum;
    }

    public boolean isDownload(long creativeId) {
        int base = new Random(creativeId).nextInt(10);
        return new Random().nextInt(57 + base) < base + magicNum;
    }

    public boolean isinstalled(long creativeId) {
        int base = new Random(creativeId).nextInt(10);
        return new Random().nextInt(67 + base) < base + magicNum;
    }


    public boolean isPayed(long creativeId) {
        int base = new Random(creativeId).nextInt(10);
        return new Random().nextInt(83 + base) < base + magicNum;
    }

    public static void main(String[] args) throws InterruptedException {
        MockData mockData = new MockData();
        while(true) {
            mockData.mock();
            Thread.sleep(100);
        }
    }
}
