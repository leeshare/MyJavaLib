package chapter05

object Test13_Lazy {
  def main(args: Array[String]): Unit = {
    lazy val result: Int = sum(13, 47)

    println("1 函数调用")
    println("2 result = " + result)   //求值变成了 懒加载

    def sum(a: Int, b: Int): Int = {
      println("3 sum调用")
      a + b
    }

}

}
