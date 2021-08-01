package chapter07

object Test11_CommonOp {
  def main(args: Array[String]): Unit = {
    val list = List(1,3,5,7,2,88)
    val set = Set(23, 34, 423, 75)

    println(list.length)
    println(list.size)
    println(set.size)

    //4 遍历
    for(elem <- list) println(elem)

    set.foreach(println)

    for(elem <- list.iterator) println(elem)

    //5 生成字符串
    println(list)
    println(set)
    println(list.mkString(", "))
    //6 是否包含
    println(list.contains(23))
    println(set.contains(23))



}

}
