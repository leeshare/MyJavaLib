package com.lixl.stream.ad;

import com.lixl.stream.entity.AdServerLog;
import com.lixl.stream.utils.Constants;
import com.lixl.stream.utils.ETLUtils;
import com.lixl.stream.utils.HBaseUtils;
import com.lixl.stream.utils.RedisUtils;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;
import org.apache.hadoop.hbase.client.HTable;
import redis.clients.jedis.Jedis;

public class AdServerLogRichFlatMap extends RichFlatMapFunction<AdServerLog, String> {
    String tableName = Constants.TABLE_NAME;
    HTable hTable;
    Jedis jedis;
    //过期时间为 1天
    int expire = 1 * 24 * 60 * 60;

    @Override
    public void open(Configuration parameters) throws Exception {
        //创建表
        hTable = HBaseUtils.initHBaseClient(tableName);
        jedis = RedisUtils.initRedis();
        super.open(parameters);
    }

    /**
     * 把输入的value，展开，输出到 Collector
     * @param adServerLog
     * @param collector
     * @throws Exception
     */
    @Override
    public void flatMap(AdServerLog adServerLog, Collector<String> collector) throws Exception {
        byte[] key = ETLUtils.generateBytesKey(adServerLog);
        AdServerLog context = ETLUtils.generateContext(adServerLog);
        ETLUtils.writeRedis(jedis, key, context);
        ETLUtils.writeHBase(hTable, key, context);
    }
}
