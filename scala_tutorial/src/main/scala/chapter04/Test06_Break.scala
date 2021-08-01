package chapter04

import scala.util.control.Breaks
import scala.util.control.Breaks._

object Test06_Break {

  def main(args: Array[String]): Unit = {
    try {
      for(i <- 0 until 5){
          if(i == 3)
            throw new RuntimeException
        println(i)
      }
    } catch {
      case e: Exception =>    //什么都不做，只是退出循环
    }

    //简化
    Breaks.breakable(
      for(i <- 0 until 5){
        if(i == 3)
          Breaks.break()
        println(i)
      }
    )

    //再简化
    breakable(
      for(i <- 0 until 5){
        if(i == 3)
          break()
        println(i)
      }
    )


    println("这是循环外代码")
  }

}
