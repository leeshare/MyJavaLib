package lesson01

import org.apache.flink.streaming.api.scala._

object WordCount3 {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val data = env.socketTextStream("192.168.152.102", 9999)
    data.flatMap( line => line.split(","))
        .map((_,1))
        .keyBy(0)
        .sum(1)
        .print()
    env.execute("wordcount")
  }

}
