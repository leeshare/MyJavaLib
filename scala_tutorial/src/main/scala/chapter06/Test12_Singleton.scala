package chapter06

object Test12_Singleton {
  def main(args: Array[String]): Unit = {
    println("=======单例模式=======")
    val student11 = Student12.getInstance()
    student11.printInfo()

    val student22 = Student12.getInstance()
    student22.printInfo()
    println(student11 == student22)
}

}


class Student12 private(val name: String, val age: Int) {
  def printInfo(): Unit ={
    println(s"student ${name} ${age}")

  }
}
//饿汉模式
/*object Student12 {
  private val student: Student12 = new Student12("alice", 28)
  def getInstance(): Student12 = student
}*/
//懒汉模式
object Student12 {
  private var student: Student12 = _
  def getInstance(): Student12 = {
    if(student == null){
      student = new Student12("alice", 38)
    }
    student
  }
}