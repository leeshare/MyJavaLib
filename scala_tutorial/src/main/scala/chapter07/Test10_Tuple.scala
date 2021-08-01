package chapter07

/**
 * 元组
 */
object Test10_Tuple {
  def main(args: Array[String]): Unit = {
    //1 创建
    val tuple: (String, Int, Char, Boolean) = ("hello", 100, 'a', true)
    println(tuple)  //调用自身实现的 toString

    println(tuple._1)
    println(tuple._4)
    println(tuple.productElement(0))

    //3 遍历
    for(elem <- tuple.productIterator) {
      println(elem)
    }

    //4 嵌套
    val mulTuple = (12, 0.3, "hello", (23, "scala"), 29)
    println(mulTuple._4._2)

}

}
