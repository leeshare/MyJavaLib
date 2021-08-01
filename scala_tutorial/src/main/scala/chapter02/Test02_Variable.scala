package chapter02

import chapter01.Student

/**
 * Scala 是强类型语言
 *    类型确定后，就不能修改
 *
 * 变量声明时，必须有初始值
 *
 * 引用类型
 */
object Test02_Variable {
  def main(args: Array[String]): Unit = {
    var a: Int = 10
    var a1 = 10
    val b1 = 23

    val alice = new Student("alice", 20)
    //alice = new Student("Alice", 25)
    var bob = new Student("bob", 23)
    bob.age = 24
    bob.printInfo()
    //以关键字作为变量
    val `try` = "ni hao 我叫try"
    println(`try`)
    //以关键字作为方法名
    val obj = new Test02_Variable()
    val result = obj.`if`("li")
    println(result)
    //以操作符开头，且只能包含（+ - * / # ! % ）
    val -+/% = "hello"
    println(-+/%)
  }

}

class Test02_Variable {

  def `if`(name: String) : String = {
    var result: String = "加强";
    if(name != null) {
      result += name
    }
    return result
  }
}
