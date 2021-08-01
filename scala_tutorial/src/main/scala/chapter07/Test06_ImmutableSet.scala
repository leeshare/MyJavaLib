package chapter07

object Test06_ImmutableSet {
  def main(args: Array[String]): Unit = {
    //1 create
    val set1 = Set(13, 20, 20, 44, 81)    //可以数据去重
    println(set1)

    //2 add
    val set2 = set1 + 28    //set是无序的
    println(set1)
    println(set2)

    //3 合并set
    val set3 = Set(19, 13, 67, 80)
    val set4 = set2 ++ set3
    println(set2)
    println(set3)
    println(set4)

    //4 del
    val set5 = set4 - 13
    println(set5)

}

}
