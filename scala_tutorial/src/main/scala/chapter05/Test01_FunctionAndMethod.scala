package chapter05

object Test01_FunctionAndMethod {

  def main(args: Array[String]): Unit = {

    //叫狭义函数
    def sayHi(name: String): Unit = {
      println("hi, " + name)
    }

    sayHi("lee")

    Test01_FunctionAndMethod.sayHi("bob")

  }

  def sayHi(name: String): Unit = {
    println("hi hi, " + name)
  }

}
