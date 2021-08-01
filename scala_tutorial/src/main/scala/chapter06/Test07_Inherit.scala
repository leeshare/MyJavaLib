package chapter06

object Test07_Inherit {
  def main(args: Array[String]): Unit = {

    val student1 = new Student7("alice", 18)
    val student2 = new Student7("bob", 20)

    val teacher = new Teacher
    teacher.printInfo()

    def personInfo(person: Person7): Unit = {
      person.printInfo()
    }

    personInfo(student1)
    personInfo(teacher)
    val person = new Person7
    personInfo(person)
}

}

class Person7 {
  var name: String = _    //给个默认初始值，否则 子类调用 printInfo 时，这里 name 和 age 未初始化 就报错了
  var age: Int = _

  println("1 父类的主构造器调用")

  def this(name: String, age: Int) {
    this()
    println(s"2 父类辅助构造器 ${name} ${age}")
    this.name = name
    this.age = age

  }

  def printInfo(): Unit = {
    println(s"Person: ${name} ${age}")
  }

}

class Student7(name: String, age: Int) extends Person7(name, age) {
  var stuNo: String = _

  println("3 子类的主构造器调用")

  def this(name: String, age: Int, stuNo: String) {
    this(name, age)
    println("4 子类的辅助构造器调用")
    this.stuNo = stuNo
  }

}

class Teacher extends Person7 {
  override def printInfo(): Unit = {
    println(s"Teacher ")
  }

}
