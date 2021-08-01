package chapter04

import scala.io.StdIn

object Test01_IfElse {
  def main(args: Array[String]): Unit = {
    println("please input:")
    val age: Int = StdIn.readInt()

    if(age >= 18) {
      println("adult")
    }


  }

}
