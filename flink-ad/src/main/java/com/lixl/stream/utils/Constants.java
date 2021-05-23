package com.lixl.stream.utils;

public class Constants {
    public static String SERVER_LOG = "server_log_v30";
    public static String CLIENT_LOG = "client_log_v30";
    public static String CLIENT_LOG_RETRY = "client_log_retry_v30";
    public static String AD_LOG = "ad_log_v30";
    public static String AD_LOG_REPORT = "ad_log_report_v30";
    public static String TABLE_NAME = "context_v30";
    public static String HDFS_LOG_HOME = "/home/";
    public static String BROKERS = "bigdata03:9092,bigdata04:9092";

    //redis 过期时间 1小时
    static final int DEFAULT_EXPIRE = 1 * 60 * 60;

    static final String ZK_CONNECT = "bigdata02:2181,bigdata03:2181,bigdata04:2181";

    static final String REDIS_ADDR = "bigdata03";
    static final int REDIS_PORT = 6379;
}
