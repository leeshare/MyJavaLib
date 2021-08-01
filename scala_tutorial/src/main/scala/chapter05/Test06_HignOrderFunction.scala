package chapter05

object Test06_HignOrderFunction {
  def main(args: Array[String]): Unit = {
    def f(n: Int): Int = {
      println("f调用")
      n + 1
    }

    val result: Int = f(123)
    println(result)

    //1 函数作为值进行传递
    val f1: Int=>Int = f
    val f2= f _

    println(f1)
    println(f1(12))
    println(f2)
    println(f2(23))

    println(f1 == f2)

    def fun(): Int = {
      println("fun调用")
      1
    }
    val f3 = fun    //函数调用
    val f4 = fun _  //函数本身
    println(f3)
    println(f4)

    //2 函数作为参数进行传递
    def dualEval(op: (Int, Int)=>Int, a: Int, b: Int) = {
      op(a, b)
    }

    def add(a: Int, b: Int): Int = {
      a + b
    }

    println(dualEval(add, 3, 99))
    println(dualEval((a, b)=>a + b, 5, 18))
    println(dualEval(_ + _, 5, 18))

    //3 函数作为返回值进行传递   函数嵌套
    def f5(): Int=>Unit = {
      def f6(a: Int): Unit = {
        println("f6 调用")
      }

      //f6 _ 或
      f6
    }

    val f6 = f5()
    println(f6)
    println(f6(8))

    println(f5()(8))



}

}
