package chapter04

import scala.collection.parallel.immutable

object Test03_MulTable {
  def main(args: Array[String]): Unit = {

    //引入变量
    for (i <- 1 to 10; j = 10 - i) {
      println("i = " + i + ", j = " + j)
    }
    for{
      i <- 1 to 10
      j = 10 - i
    } {
      println("i = " + i + ", j = " + j)
    }

    println("============= yield ============ ")
    val b = for(i <- 1 to 10) yield i * 2
    println("b = " + b)

    //val c: immutable.IndexedSeq[Int] = for(i <- 1 to 10) yield i * 2
    //println("c = " + c)

  }

}
