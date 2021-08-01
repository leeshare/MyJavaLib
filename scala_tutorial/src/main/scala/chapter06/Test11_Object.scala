package chapter06

object Test11_Object {
  def main(args: Array[String]): Unit = {
    //val student = new Student11("alice", 18)
    val student = Student11.newStudent("alice", 18)
    student.printInfo()

    val student2 = Student11.apply("alice", 18)
    student2.printInfo()
    //apply方法 名称可以省略
    val student3 = Student11("alice", 18)
    student3.printInfo()



}

}

//把构造函数定义为私有，则在其他类中 无法调用了，但还是可以通过伴生对象来调用
class Student11 private (val name: String, val age: Int) {
  def printInfo(): Unit ={
    println(s"student: name = ${name} ${age} ${Student11.school}")
  }
}

object Student11 {
  val school: String = "bj"

  //定义一个类的对象实例的创建方法
  def newStudent(name: String, age: Int) : Student11 = new Student11(name, age)

  //scala底层 会对 apply名字进行优化
  def apply(name: String, age: Int) : Student11 = new Student11(name, age)
}

