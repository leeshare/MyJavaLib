package chapter05

object Test11_ControlAbstraction {
  def main(args: Array[String]): Unit = {

    //1 传值参数 ————就是我们平常的使用方式
    def f0(a: Int): Unit = {
      println("a: " + a)
      println("a: " + a)
    }

    def f1(): Int = {
      println("f1调用")
      12
    }
    f0(f1())

    //2 传名参数   传递不再是具体的值， 而是 传一部分可执行的代码块
    def f2(a: => Int): Unit = {
      println("a: " + a)
      println("a: " + a)
      println("a: " + a)
    }
    f2(23)  //23也算一个代码块
    f2(f1())  //执行次数 由抽象控制
    f2({
      println("这是一个代码块")
      22
    })


}

}
