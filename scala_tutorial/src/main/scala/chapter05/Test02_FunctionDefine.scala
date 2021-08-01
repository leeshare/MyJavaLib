package chapter05

object Test02_FunctionDefine {

  def main(args: Array[String]): Unit = {

    def f1(): Unit = {
      println("1 无参 无返回值")
    }
    println(f1())

    def f2(): Int = {
      println("2 无参 有返回值")
      return 99
    }
    println(f2())

    def f3(name: String): Unit = {
      println("3 有参 无返回值" + name)
    }
    println(f3("alice"))

    def f4(name: String): String = {
      println("4 有参 有返回值" + name)
      return name
    }
    println(f4("bob"))

    def f5(name1: String, name2: String): Unit = {
      println("5 多参 无返回值")
      println(s"${name1} and ${name2} are good friends ")
    }
    println(f5("alice", "bob"))

    def f6(a: Int, b: Int): Int= {
      println("6 多参 有返回值")
      return a + b
    }
    println(f6(6, 8))

  }

}
