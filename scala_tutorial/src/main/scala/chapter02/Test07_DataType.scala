package chapter02

import chapter01.Student

object Test07_DataType {
  def main(args: Array[String]): Unit = {
    val a1: Byte = -128
    val a2: Long = 300009999999L

    val b1: Short = 10
    val b2: Short = (10000 + 20000).toShort
    println(b2)

    val f1: Float = 1.2345f
    var f2 = 34.2233

    val c1: Char = 'a'
    println(c1 + " ASICC数值= " + (c1 + 0))
    val c2: Char = '9'
    println(c2 + " ASICC数值= " + (c2 + 0))
    println(c2 + c1)
    val i1: Int = c1;
    val c11: Char = (i1 + 1).toChar
    println(c11)
    val c3: Char = '\t'   //制表符
    val c4: Char = '\n'   //换行符
    println("abc" + c3 + "def")
    println("abc" + c4 + "def")


    val isTrue: Boolean = true;
    println(isTrue)

    def ml(): Unit = {
      println("ml function")
    }

    val a = ml()
    println("a: " + a)

    var student : Student = new Student("alice", 23)
    student = null
    println(student)

    /**
     * 发生异常，直接抛异常 ————其实就是返回了 Nothing
     * 不发生异常，就直接返回定义的值
     * @param n
     * @return
     */
    def m2(n: Int): Int = {
      if(n == 0)
        throw new NullPointerException
      return n
    }

    val b = m2(2)
    println(b)
  }

}
