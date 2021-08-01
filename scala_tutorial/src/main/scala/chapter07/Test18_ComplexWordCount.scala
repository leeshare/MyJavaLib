package chapter07

object Test18_ComplexWordCount {

  def main(args: Array[String]): Unit = {
    val tupleList: List[(String, Int)] = List(
      ("hello", 1),
      ("hello word", 2),
      ("hello scala", 3),
      ("hello spark from scala", 1),
      ("hello flink from scala", 2)
    )

    //一 普通方式
    val newStringList: List[String] = tupleList.map(
      kv => {
        (kv._1.trim + " ") * kv._2
      }
    )
    println(newStringList)

    val wordCountList = newStringList.flatMap(_.split(" "))
      .groupBy(word => word)
      .map(kv => (kv._1, kv._2.size))
      .toList
      .sortBy(_._2)(Ordering[Int].reverse)
      .take(3)
    println(wordCountList)

    //二 基于预统计结果
    //1 字符串打散为单词
    val preCountList: List[(String, Int)] = tupleList.flatMap(
      tuple => {
        val strings: Array[String] = tuple._1.split(" ")
        strings.map(word => (word, tuple._2))
      }
    )
    println(preCountList)
    //2 对二元组按照单词进行分组
    val preCountMap = preCountList.groupBy( tuple => tuple._1 )
    println(preCountMap)
    //3 叠加每个单词预统计的个数值
    val countMap: Map[String, Int] = preCountMap.mapValues(
      tupleList => tupleList.map(_._2).sum
    )
    println(countMap)

    val countList = countMap.toList
      .sortWith( _._2 > _._2 )
      .take(3)
    println(countList)




  }
}
