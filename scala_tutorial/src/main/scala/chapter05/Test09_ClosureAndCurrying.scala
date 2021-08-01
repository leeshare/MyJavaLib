package chapter05

/**
 * 闭包 和 柯里化
 */
object Test09_ClosureAndCurrying {
  def main(args: Array[String]): Unit = {

    def add(a: Int, b: Int): Int = {
      a + b
    }

    //1 考虑固定一个加数的场景
    def addByFour(b: Int): Int = {
      4 + b
    }
    //2 扩展固定加数改变的情况
    def addByFive(b: Int): Int = {
      5 + b
    }
    //3 当固定加数作为另一参数传入，但是作为“第一层参数”传入
    def addByFour1(): Int=>Int = {
      val a = 4
      def addB(b: Int):Int = {
        a + 4
      }
      addB
    }

    def addByA(a: Int): Int=>Int = {
      def addB(b: Int): Int = {
        a + b
      }
      addB
    }

    println(addByA(35)(25))

    val addByFour2 = addByA(4)
    val addByFive2 = addByA(5)

    println(addByFour2(6))
    println(addByFive2(15))
    //闭包 就是通用性 和 适应性的平衡

    //4 lambda表达式简写
    def addByA2(a: Int): Int=>Int = a + _
    println(addByA2(8)(22))


    //5 柯里化
    def addCurrying(a: Int)(b: Int): Int = {
      a + b
    }
    println(addCurrying(9)(8))

  }

}
