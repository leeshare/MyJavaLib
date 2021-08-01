package chapter06

object Test03_Class {
  def main(args: Array[String]): Unit = {
    val student = new Student();
    println(student.age)
    println(student.sex)
    student.age = 18
    println(student.age)

}

}

class Student {
  private val name: String = "hello"
  var age: Int = _  //表示初始值是 0
  var sex: String = _   //表示初始值是 空

}
