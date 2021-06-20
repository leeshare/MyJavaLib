package org

//import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._

object WordCount {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val data = env.socketTextStream("bigdata02", 9999)
    data.flatMap( line => line.split(","))
      .map((_,1)).keyBy(0).sum(1).print()
    env.execute("wordcount")

  }

}
