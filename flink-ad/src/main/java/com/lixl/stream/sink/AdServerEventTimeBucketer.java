package com.lixl.stream.sink;

import com.lixl.stream.entity.AdServerLog;
import org.apache.flink.streaming.connectors.fs.Clock;
import org.apache.flink.streaming.connectors.fs.bucketing.Bucketer;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 *
 * 接口 Bucketer 是用来把 每一个元素 放到其应该归档的文件下
 *      其有2个主要实现：
 *          BasePathBucketer    把所有文件写入指定文件下
 *          DateTimeBucketer    把发送到buckets的数据按照当前系统时间（处理时间），将创建目录，按 /{basePath}/{dateTimePath}/
 *      这两个主要实现，都不满足我们的业务，我们要按照事件创建时间
 *      所以，我们仿照 DateTimeBucketer来创建一个自己的 Bucketer
 */
public class AdServerEventTimeBucketer implements Bucketer<AdServerLog> {
    private static final String DEFAULT_FORMAT_STRING = "yyyyMMdd";
    private final String formatString;
    private final ZoneId zoneId;
    private transient DateTimeFormatter dateTimeFormatter;

    public AdServerEventTimeBucketer(){
        this(DEFAULT_FORMAT_STRING);
    }
    public AdServerEventTimeBucketer(String formatString) {
        this(formatString, ZoneId.systemDefault());
    }
    public AdServerEventTimeBucketer(ZoneId zoneId) {
        this(DEFAULT_FORMAT_STRING, zoneId);
    }
    public AdServerEventTimeBucketer(String formatString, ZoneId zoneId) {
        this.formatString = formatString;
        this.zoneId = zoneId;
        this.dateTimeFormatter = DateTimeFormatter.ofPattern(this.formatString).withZone(this.zoneId);
    }

    //记住，这个方法一定要加，否则dateTimeFormatter对象会是空，此方法会在反序列的时候调用，这样才能正确初始化dateTimeFormatter对象
    //那有的人问了，上面构造函数不是初始化了吗？反序列化的时候是不走构造函数的
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.dateTimeFormatter = DateTimeFormatter.ofPattern(formatString).withZone(zoneId);
    }

    @Override
    public Path getBucketPath(Clock clock, Path basePath, AdServerLog adServerLog) {
        String newDateTimeString = dateTimeFormatter.format(Instant.ofEpochMilli(adServerLog.getTimestamp()));
        return new Path(basePath + "/" + newDateTimeString);
    }
}
