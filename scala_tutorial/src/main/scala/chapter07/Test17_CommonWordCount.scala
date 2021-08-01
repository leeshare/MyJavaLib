package chapter07

object Test17_CommonWordCount {
  def main(args: Array[String]): Unit = {
    val stringList: List[String] = List(
      "hello",
      "hello word",
      "hello scala",
      "hello spark from scala",
      "hello flink from scala"
    )

    val wordList1: List[Array[String]] = stringList.map( _.split(" "))
    val wordList2 = wordList1.flatten
    println(wordList2)

    val wordList = stringList.flatMap(_.split(" "))
    println(wordList)

    val groupMap: Map[String, List[String]] = wordList.groupBy(word => word)
    println(groupMap)

    val countMap: Map[String, Int] = groupMap.map(kv => (kv._1, kv._2.length))
    println(countMap)

    val sortList: List[(String, Int)] = countMap.toList
      .sortWith( _._2 > _._2 )
        .take(3)
    println(sortList)

}

}
