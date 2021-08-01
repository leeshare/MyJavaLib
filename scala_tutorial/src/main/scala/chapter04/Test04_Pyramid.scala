package chapter04

object Test04_Pyramid {

  def main(args: Array[String]): Unit = {

    for(i <- 1 to 9; j = 9 - i) {
      val space = " "
      print(space * j)
      val star = "*"
      val num = 2 * (i - 1) + 1
      println(star * num)
    }

  }

}
