package chapter02

/**
 * 原因是：类型强转时，直接粗暴的截取了一部分数据
 *
 * 128: Int类型，占据4个字节，32位
 * 源码： 0000 0000 0000 ... 1000 0000
 * 补码： 0000 0000 0000 ... 1000 0000
 *
 * 截取最后一个字节 to Byte
 *    1000 0000   这是个补码表达（第一位是符号位 表示负数）  这里就是最大的负数 -128
 *
 * 130
 * 源码： 0000 0000 ... 1000 0010
 * 补码： 0000 0000 ... 1000 0010
 * 截取最后一个字节 to Byte
 *    1000 0010   这是补码表达，表示的数是：   -128 + 2 = -126
 * 求源码 = 补码 + 1
 *    1111 1110  = -( 2^6 + 2^5 + 2^4 + 2^3 + 2^2 + 2^1 ) = -126
 *
 */
object Test09_Problem_DataTypeConvertion {
  def main(args: Array[String]): Unit = {
    val n: Int = 128
    val b: Byte = n.toByte    //-128
    println(b)
    val n1: Int = 130
    val b1: Byte = n.toByte    //-126
    println(b1)

  }

}
