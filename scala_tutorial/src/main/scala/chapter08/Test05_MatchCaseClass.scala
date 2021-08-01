package chapter08

/**
 * 对于 做 对象实例内容的 一个模式匹配 ———— 样例类是最方便快捷的
 *
 */
object Test05_MatchCaseClass {
  def main(args: Array[String]): Unit = {
    val student = new Student1("alice", 18)

    val result = student match {
      case Student1("alice", 18) => "Alice, 18"
      case _ => "Else"
    }

    println(result)

  }

}

//样例类
case class Student1(name: String, age: Int)
