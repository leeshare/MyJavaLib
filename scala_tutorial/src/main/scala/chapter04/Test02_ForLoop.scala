package chapter04

object Test02_ForLoop {
  def main(args: Array[String]): Unit = {

    for(i <- 1 to 10){
      println(i + "次 hello world")
    }
    for(i <- 1.to(10)){
      println(i + "次 hello world")
    }
    for(i <- new Range(1, 10, 1)){    //不包含右边界
      println(i + "次 hello world")
    }
    println("-------")
    for(i <- 1 until 10){     //不包含右边界
      println(i + "次 hello world")
    }

    //集合
    println("-----集合------")
    for(i <- Array(12, 34, 53)){
      println(i)
    }
    for(i <- List(12, 34, 53)){
      println(i)
    }
    for(i <- Set(12, 34, 53)) {
      println(i)
    }
    println("----循环守卫-----")
    for(i <- 1 to 10 if i != 5) {
      println(i)
    }
    println("---循环步长---")
    for(i <- new Range(1, 11, 2)){    //不包括右边界
      println(i)
    }
    for(i <- 1 to 11 by 2){
      println(i)
    }
    for(i <- 30 to 9 by -3){
      println(i)
    }
    for(i <- 1 to 10 reverse){
      println(i)
    }
    for(i <- 1.0 to 10 by 0.5){
      println(i)
    }
  }

}
