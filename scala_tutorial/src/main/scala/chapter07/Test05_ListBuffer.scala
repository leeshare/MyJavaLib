package chapter07

import scala.collection.mutable.ListBuffer

/**
 * 可变数组
 */
object Test05_ListBuffer {
  def main(args: Array[String]): Unit = {
    //1 create
    val list1: ListBuffer[Int] = new ListBuffer[Int]()
    val list2 = ListBuffer(12, 33, 66)

    //2 add element
    list1.append(15, 30)
    list2.prepend(10)
    list1.insert(1, 16, 17, 19)

    println(list1)
    println(list2)

    31 +=: 96 +=: list1 += 25 += 11
    println(list1)

    println("=========")
    val list3 = list1 ++ list2
    println(list1)
    println(list2)
    println(list3)

    println("======list2 改变；list1 不变===")
    println(list1)
    println(list2)
    list1 ++=: list2
    println(list1)
    println(list2)

    //4 modify
    list2(3) = 30
    list2.update(4, 31)
    println(list2)

    //5 remove
    list2.remove(4)
    list2 -= 30   //只移除了第一个30
    println(list2)

}

}

