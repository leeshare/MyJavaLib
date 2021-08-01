package chapter06

object Test09_AbstractClass {
  def main(args: Array[String]): Unit = {
    val student = new Student9
    student.eat()
    student.sleep()

  }

}

abstract class Person9 {
  val name: String = "person"
  var age: Int = _
  //非抽象方法
  def eat(): Unit = {
    println("person eat")
  }
  //抽象方法
  def sleep(): Unit
}

class Student9 extends Person9 {
  def sleep(): Unit = {
    println("student sleep")
  }

  override def eat(): Unit = {
    super.eat()
    println("student eat")
  }

  override val name: String = "student"
  //只能重写 val 的属性，不能重写 var 的属性

  age = 18
}
