package com.zeye.networkflow

import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.watermark.Watermark
import org.apache.flink.streaming.api.windowing.time.Time


// 定义输入数据的样例类
case class UserBehavior( userId: Long, itemId: Long, categoryId: Int,
    behavior: String, timestamp: Long )


object PageView {

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    env.setParallelism(1)

    env.readTextFile("C:\\Users\\mazhonghua\\Desktop\\Flink上课资料\\myZeye\\src\\main\\resources\\data1.csv").map(line => {
      val fields = line.split(",")
      UserBehavior(fields(0).trim.toLong, fields(1).trim.toLong, fields(2).trim.toInt, fields(3).trim, fields(4).trim.toLong)
    }).assignTimestampsAndWatermarks(new PageViewEventTimeExtractor())
        .filter(_.behavior == "pv")
        .map( data => ("pv",1))
        .timeWindowAll(Time.hours(1))
        .sum(1)
        .print()

    env.execute("PageView")
  }

}

/**
 * 定义waterMark
 */
class PageViewEventTimeExtractor extends  AssignerWithPeriodicWatermarks[UserBehavior] {


  var currentMaxEventTime = 0L
  val maxOufOfOrderness = 10000 //最大乱序时间

  override def getCurrentWatermark: Watermark = {
    new Watermark(currentMaxEventTime - maxOufOfOrderness)
  }

  override def extractTimestamp(element: UserBehavior, previousElementTimestamp: Long): Long = {
    //时间字段
    val timestamp = element.timestamp * 1000

    println(element.toString)
    currentMaxEventTime = Math.max(element.timestamp, currentMaxEventTime)
    timestamp;
  }
}
