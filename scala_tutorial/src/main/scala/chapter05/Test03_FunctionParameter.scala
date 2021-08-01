package chapter05

object Test03_FunctionParameter {

  def main(args: Array[String]): Unit = {
    //1 可变参数
    def f1(str: String*): Unit = {
      println(str)    //str 是 包装起来的数组
    }
    f1("aa", "bb", "cc")

    //2 可变参数必须放在最后
    def f2(str1: Int, str2: String*): Unit = {
      println(str1 + " " + str2)
    }
    f2(88, "cc", "dd")

    //3 参数默认值
    def f3(name: String = "lee"): Unit = {
      println("My school is " + name)
    }
    f3("bj")
    f3()

    //4 带名参数
    def f4(name: String, age: Int): Unit = {
      println(s"${age}岁的${name}在学习中")
    }
    f4(age = 23, name="alice")
    def f44(name: String = "lee", age: Int): Unit = {
      println(s"${age}岁的${name}在学习中")
    }
    f44(age = 36)

  }

}
