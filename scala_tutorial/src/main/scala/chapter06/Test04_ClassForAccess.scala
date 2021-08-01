package chapter06

object Test04_ClassForAccess {

}

//定义一个父类
class Person {
  private var idCard: String = "332332"   //私有属性 无法访问
  protected var name: String = "alice"    //保护属性 只能在当前类和子类
  var sex: String = "female"
  private[chapter06] var age: Int= 18

  def printInfo(): Unit = {
    println(s"Person: ${idCard} ${name}")
  }

}
