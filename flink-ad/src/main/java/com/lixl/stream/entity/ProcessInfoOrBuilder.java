// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: ad_log.proto

package com.lixl.stream.entity;

public interface ProcessInfoOrBuilder extends
    // @@protoc_insertion_point(interface_extends:grpc.ProcessInfo)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   *是否关联上了
   * </pre>
   *
   * <code>optional bool join_server_log = 1;</code>
   */
  boolean getJoinServerLog();

  /**
   * <pre>
   *处理时间
   * </pre>
   *
   * <code>optional int64 process_timestamp = 2;</code>
   */
  long getProcessTimestamp();

  /**
   * <pre>
   *重试次数
   * </pre>
   *
   * <code>optional int64 retry_count = 3;</code>
   */
  long getRetryCount();
}
