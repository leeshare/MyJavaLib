package com.lixl.stream.sink;

import com.lixl.stream.entity.AdClientLog;
import org.apache.flink.streaming.connectors.fs.Clock;
import org.apache.flink.streaming.connectors.fs.bucketing.Bucketer;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class AdClientEventTimeBucketer implements Bucketer<AdClientLog> {

    private static final String DEFAULT_FORMAT_STRING = "yyyyMMdd";

    private final String formatString;

    private final ZoneId zoneId;
    private transient DateTimeFormatter dateTimeFormatter;

    public AdClientEventTimeBucketer() {
        this(DEFAULT_FORMAT_STRING);
    }

    public AdClientEventTimeBucketer(String formatString) {
        this(formatString, ZoneId.systemDefault());
    }

    public AdClientEventTimeBucketer(ZoneId zoneId) {
        this(DEFAULT_FORMAT_STRING, zoneId);
    }

    public AdClientEventTimeBucketer(String formatString, ZoneId zoneId) {
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
    public Path getBucketPath(Clock clock, Path basePath, AdClientLog adClientLog) {
        String newDateTimeString = dateTimeFormatter.format(Instant.ofEpochMilli(adClientLog.getTimestamp()));
        return new Path(basePath + "/" + newDateTimeString);
    }

}
