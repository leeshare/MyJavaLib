package chapter06

/**
 * Student3 的写法 是最为推荐的写法
 *      class Student3(var name: String, var age: Int)
 */
object Test06_ConstructorParams {
  def main(args: Array[String]): Unit = {
    val student2 = new Student2()
    println(s"student2 name= ${student2.name} age = ${student2.age}")

    val student3 = new Student3("bob", 20)
    println(s"student3 name= ${student3.name} age = ${student3.age}")

    val student4 = new Student4("bob", 20)
    //println(s"student4 name= ${student4.name} age = ${student4.age}")
    student4.printStudent()


  }

}

class Student2 {
  var name: String = _
  var age: Int = _
}

//等价于上面 Student2的定义
class Student3(var name: String, var age: Int)
//最推荐这样写


class Student4(name: String, age: Int) {
  def printStudent(): Unit = {
    println(s"student4 name= ${name} age = ${age}")
  }
}