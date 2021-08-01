package chapter03

object Test01_TestOperator {
  def main(args: Array[String]): Unit = {
    val result: Double = 10 / 3
    println(result)
    val result2 = 10.0 / 3
    println(result2)
    println(result2.formatted("%5.2f"))

    val s1: String = "hello"
    val s2: String = new String("hello")
    println(s1 == s2) //就是equals
    println(s1.equals(s2))
    println(s1.eq(s2)) //对象判断

    def isNotEmpty(str: String): Boolean = {
      return str != null && !("".equals(str.trim))
    }

    println(isNotEmpty(null))


    var b: Int = 10;
    b += 1
    println(b)

    val a = 60
    val c: Byte = (a << 3).toByte
    println(c)
    println(a>>2)

    val ss: Short = -13
    println(ss << 2)
    println(ss >> 2)    //左边补符号位
    println(ss >>> 2)   //左边补0

    println("=============================")
    val n1: Int = 12
    val n2: Int = 37
    println(n1.+(n2))
    println(n1 + (n2))
    println(n1 + n2)

    println(1.34.*(25))

    println(7.5 toString)
    println(7.5 toInt)

  }

}
