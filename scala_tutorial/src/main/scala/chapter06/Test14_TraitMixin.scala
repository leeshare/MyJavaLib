package chapter06

object Test14_TraitMixin {
  def main(args: Array[String]): Unit = {
    val student = new Student14()
    student.study()
    student.increase()

    student.play()
    student.increase()

    student.dating()
    student.increase()

    //动态混入
    println("===========动态混入===========")
    val studentWithTalent = new Student14 with Talent {
      override def dancing(): Unit = println(" dancing")
      override def singing(): Unit = println("singing ")
    }
    studentWithTalent.dancing()
    studentWithTalent.singing()

  }

}

trait Knowledge {
  var amount: Int = 0
  def increase(): Unit
}

trait Talent {
  def singing(): Unit
  def dancing(): Unit
}

class Student14 extends Person13 with Young with Knowledge {
  //重写冲突的属性
  override val name: String = "student"

  def dating(): Unit = println(s"student ${name} is dating ")

  def study(): Unit = println(s"student ${name} is studying")

  override def sayHello(): Unit = {
    super.sayHello()
    println(s"hello  from ${name}")
  }

  override def increase(): Unit = {
    amount += 1
    println(s"student ${name} knowledge is ${amount}")
  }
}

