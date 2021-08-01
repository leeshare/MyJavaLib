package chapter07

import scala.collection.mutable

object Test07_MutableSet {
  def main(args: Array[String]): Unit = {
    //1 create
    val set1 = mutable.Set[Int](13, 23, 53, 12, 13, 23, 78)
    println(set1)

    val set2 = set1 + 11
    println(set1)
    println(set2)

    set1 += 11
    set1 -= 78
    println(set1)

    val flag1 = set1.add(10)
    println(set1)
    println(flag1)

    val flag2 = set1.add(10)
    println(set1)
    println(flag2)

    set1.remove(11)
    println(set1)

    //4 合并
    val set3 = mutable.Set(13, 99, 23, 98)
    val set4 = set1 ++ set3
    println(set1)
    println(set3)
    println(set4)

    set1 ++= set3
    println(set1)
    println(set3)

}

}
