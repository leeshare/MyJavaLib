package chapter07

/**
 * 集合计算函数
 */
object Test13_SimpleFunction {
  def main(args: Array[String]): Unit = {
    val list = List(5, 1, 8, -3, 4)
    val list2 = List(("a", 5),("b", 1),("c", 8),("d", -3),("e", 4))

    // 1 求和
    var sum = 0;
    for(elem <- list) {
      sum += elem
    }
    println(sum)

    println(list.sum)
    //2 求乘积
    println(list.product)
    //3 最大值
    println(list.max)

    println(list2.maxBy((tuple: (String, Int)) => tuple._2))
    println(list2.maxBy( _._2 ))
    //4 最小值
    println(list.min)
    println(list2.minBy(_._2))
    //5 排序
    val sortedList = list.sorted    //默认从小到大
    println(sortedList)
    println(sortedList.reverse)
    val sortedList2 = list.sorted(Ordering[Int].reverse)
    println(sortedList2)

    println(list2.sorted)
    println(list2.sortBy(_._2))
    println(list2.sortBy(_._2)(Ordering[Int].reverse))  //从大到小 会复杂一些

    println(list.sortWith((a: Int, b: Int) => {a < b}))
    println(list.sortWith(_ < _))
    println(list.sortWith(_ < _))

}

}
