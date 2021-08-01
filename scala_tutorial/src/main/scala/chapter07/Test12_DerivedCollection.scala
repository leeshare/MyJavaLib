package chapter07

/**
 * 集合操作
 */
object Test12_DerivedCollection {
  def main(args: Array[String]): Unit = {
    val list1 = List(1,3,5,7,2,18)
    val list2 = List(3,7,2,4,8, 19, 28, 33)

    val set1 = Set(1,3,5,7,2,18)
    val set2 = Set(3,7,2,4,8, 19)

    //1 获取集合头
    println(list1.head)
    //2 获取尾
    println(list1.tail) //去掉头，其余都是尾，是个集合
    //3 获取最后一个元素
    println(list2.last)
    //4 初始数据（不包含最后一个元素
    println(list2.init)
    //5 反转
    println(list2)
    println(list2.reverse)
    //6 去前/后几个元素
    println(list2.take(3))
    println(list2.takeRight(3))
    //7 去掉前/后几个元素
    println(list2)
    println(list2.drop(3))
    println(list2.dropRight(3))
    //8 并集
    val union = list1.union(list2)    //list 是不会去重的
    println("list union: " + union)
    println(list1 ::: list2)

    val union2 = set1.union(set2)     //set 是会去重的
    println("set union: " + union2)
    println("set union: " + (set1 ++ set2))
    //9 交集
    val intersection = list1.intersect(list2)
    println("intersection: " + intersection)
    //10 差集
    val diff1 = list1.diff(list2)
    val diff2 = list2.diff(list1)
    println("diff1: " + diff1)
    println("diff2: " + diff2)
    //11 拉链   ————得到二元组 Tuple2
    val zip1 = list1.zip(list2)
    val zip2 = list2.zip(list1)
    println("zip1：" + zip1)
    println("zip2：" + zip2)
    //12 滑窗
    println( list1)
    for(elem <- list1.sliding(3)) println(elem)     //滑动窗口
    println( list2)
    for(elem <- list2.sliding(3, 2)) println(elem)    //两个步长的滑动窗口
    println( list2)
    for(elem <- list2.sliding(3, 3)) println(elem)    //滚动窗口




}

}
