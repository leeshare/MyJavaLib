package chapter06

object Test17_Extends {
  def main(args: Array[String]): Unit = {
    val student: Student17 = new Student17("alice", 16)
    student.study()
    student.sayHi()

    val person: Person17 = new Student17("bob", 33)
    person.sayHi()

    println("student is Student17 " + student.isInstanceOf[Student17])
    println("student is Person17 " + student.isInstanceOf[Person17])
    println("person is Student17 " + person.isInstanceOf[Student17])
    println("person is Person17 " + person.isInstanceOf[Person17])
    //子类 可以是父类对象

    //但父类不能认为是子类对象

    println(WorkDay.MONDAY)
}

}

class Person17(val name: String, val age: Int) {
  def sayHi(): Unit = {
    println(s"hi from person ${name}")
  }
}

class Student17(name: String, age: Int) extends Person17(name, age) {
  override def sayHi(): Unit = {
    println(s"hi from student ${name}")
  }
  def study(): Unit = {
    println("student study")
  }
}

//定义枚举类
object WorkDay extends Enumeration {
  val MONDAY = Value(1, "Monday")
  val TUESDAY = Value(2, "Tuesday")
}
//定义应用类对象
object TestApp extends App {
  println("app start")

  type MyString = String
  val a: MyString = "abc"
  println(a)

}