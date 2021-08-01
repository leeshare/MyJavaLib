package chapter05

import scala.annotation.tailrec

object Test10_Recursion {
  def main(args: Array[String]): Unit = {
    println(fact(5))

    //@tailrec
    def fact(n: Int): Int = {
      if(n == 0)
        return 1
      fact(n-1) * n
    }
    //尾递归优化
    def tailRecursion(n: Int): Int = {
      @tailrec
      def loop(n: Int, currRes: Int) : Int = {
        if(n == 0) return currRes
        loop(n - 1, currRes * n)
      }
      loop(n, 1)
    }
    println(tailRecursion(5))
  }

}
