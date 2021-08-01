package chapter05

object Test04_Simplify {
  def main(args: Array[String]): Unit = {

    def f0(name: String): String = {
      //return name
      name
    }
    println(f0("lee"))

    def f1(name: String): String = name
    println(f1("liang"))

    def f2(name: String) = name
    println(f2("lalala"))

    def f3(name: String) {
      println(name)
    }
    println(f3("haha"))

    def f4() = {
      println("444")
    }

    f4

    def f5 {
      println("555")
    }
    f5

        //匿名函数 ===> lambda表达式
    (name: String) => {
      println("匿名函数：没有def，没有函数名：" + name)
    }
      //java的lambda表达式  () ->

  }

}
