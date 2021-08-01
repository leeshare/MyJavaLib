package chapter07

object Test14_HighLevelFunction_Map {
  def main(args: Array[String]): Unit = {
    val list = List(1,2,3,4,5,6,7,8,9)

    //1 过滤
    val filter = list.filter(elem => elem % 2 == 0)
    println(filter)
    println(list.filter( _ % 2 == 1))

    //2 map
    val list2 = for(elem <- list) yield elem * 2
    println(list2)

    println(list.map(elem => elem * 2))

    //3 扁平化
    val nestedList: List[List[Int]] = List(List(1, 2, 3), List(4, 5), List(6, 7, 8, 9))
    val flatList = nestedList(0) ::: nestedList(1) ++ nestedList(2)
    println(flatList)

    val flatList2 = nestedList.flatten
    println(flatList2)

    //4 扁平映射
    val strings: List[String] = List("hello world", "hello scala", "hello java", "study java")
    val splitList = strings.map(str => str.split(" "))
    val flatten = splitList.flatten
    println(flatten)

    val flatmap = strings.flatMap( _.split(" "))
    println(flatmap)

    //5 分割groupBy
    val groupMap = list.groupBy( _ % 2)
    val groupMap2 = list.groupBy( data => {
      if(data % 2 == 0)
        "偶数"
      else
        "奇数"
    })
    println(groupMap)
    println(groupMap2)

    //一组词汇，按照单词首字母进行分组
    val wordList = List("china", "america", "alice", "canada", "cary", "bob", "japan")
    println(wordList.groupBy( _.charAt(0) ))

}

}
