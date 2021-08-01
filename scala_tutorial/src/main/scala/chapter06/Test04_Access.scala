package chapter06

object Test04_Access {

  def main(args: Array[String]): Unit = {
    val person = new Person()
    println(person.sex)
    println(person.age)
    person.printInfo()

    val worker: Worker = new Worker()
    worker.printInfo()

}
}

//定义一个子类
class Worker extends Person {
  override def printInfo(): Unit = {
    println("Worker: ")
    name = "bob"
    age = 25
    sex = "male"

    println(s" Worker: ${name} ${age} ${sex}")

  }
}
