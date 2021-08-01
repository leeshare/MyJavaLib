package chapter02

object Test08_DataTypeConversion {

  def main(args: Array[String]): Unit = {
    val a1: Byte = 10
    val b1: Long = 2353L
    val result1: Long = a1 + b1
    val result11: Int = a1 + b1.toInt

    val a2: Byte = 10
    val b2: Int = a2
    val c2: Byte = b2.toByte

    val a3: Byte = 10
    val b3: Char = 'b'
    val c3: Byte = b3.toByte
    val d3: Int = b3

    println(d3)

    val a4: Byte = 12
    val b4: Short = 25
    val c4: Char = 'c'
    val d4: Int = a4 + b4

    println(d4)


    var num: Int = 2.7.toInt
    println(num)
    val n2: Int = (2.6+ 3.7).toInt
    println(n2)

    val n: Int = 23
    val s: String = n + ""
    println(s)

    val m: Int = "12".toInt
    val f: Float = "12.3".toFloat
    //val f2: Int = "12.3".toInt    //出错
    val f3: Int = "12.3".toFloat.toInt
    println(f3)

  }

}
