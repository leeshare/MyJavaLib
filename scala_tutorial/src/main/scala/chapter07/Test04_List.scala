package chapter07

/**
 * 不可变列表 List
 */
object Test04_List {
  def main(args: Array[String]): Unit = {
    //1 create
    val list: List[Int] = List[Int](23, 65, 87)
    println(list)

    //2 访问
    println(list(1))
    list.foreach(a => println(a))

    //3 添加
    val newList = list :+ 88
    val newList2 = 22 +: list
    println(list)
    println(newList)
    println(newList2)

    val list4 = list.::(20) //加到最前面
    println(list4)

    //
    val list5 = Nil.::(13)
    println(list5)
    // :: 用来创建已新的列表
    val list6 = 32 :: 33 :: 50 :: 40 :: Nil
    println(list6)

    val list8 = list5 :: list6
    println(list8)

    val list9 = list5 ::: list6
    println(list9)

    val list10 = list5 ++ list6
    println(list10)

}

}
