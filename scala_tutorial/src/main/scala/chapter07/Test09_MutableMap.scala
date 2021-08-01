package chapter07

import scala.collection.mutable

object Test09_MutableMap {
  def main(args: Array[String]): Unit = {
    val map1: mutable.Map[String, Int] = mutable.Map("a" -> 13, "b" -> 0, "hello" -> 2)

    map1.put("c", 5)
    map1.put("d", 1)

    println(map1)

    map1 += (("e", 7))
    map1 += (("d", 7))    //存在就修改 不存在就插入
    println(map1)

    map1.remove("d")
    println( map1.getOrElse("d", 0) )

    map1 -= "e"
    println(map1)

    map1 += (("e", 7))
    map1 += (("e", 17))
    println(map1)

    //5 合并
    val map2: Map[String, Int] = Map("aaa" -> 11, "b" -> 29, "hello" -> 80) //不可变map
    //map1 ++= map2   //合并：存在就覆盖，不存在就添加
    println(map1)
    println(map2)

    val map3: Map[String, Int] = map2 ++ map1
    println(map1)
    println(map2)
    println(map3)




}

}
