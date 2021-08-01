package chapter07

/**
 * 不可变数组 ————引用类型 ：不关心指向对象的内容（存放的值）是什么，只关心当前指向的对象不能改（包括地址，大小即占据的内存空间）
 *    所以不可变数组：对于插入、删除数组元素不能修改。但其中的元素的值是可以变的。
 */
object Test01_ImmutableArray {
  def main(args: Array[String]): Unit = {
    //1 创建数组
    // 1.1)普通方式 创建数组
    val arr: Array[Int] = new Array[Int](5)
    //  1.2) 使用 Array的伴生对象中的 apply()方法 创建一个新的数组
    val arr2 = Array(12, 37, 42, 55, 66)

    //3 读取 修改
    println(arr(0))
    println(arr(4))
    arr(0) = 8
    arr(4) = 88    //相当于调用 伴生对象中的 update()方法
    arr.update(4, 89)
    println(arr(0))
    println(arr(4))

    //4 数组遍历
    //4.1) for循环
    for(i <- 0 until arr.length){
      println(arr(i))
    }

    for(i <- arr.indices) println(arr(i))

    //4.2) 直接遍历所有元素
    for(elem <- arr2) println(elem)

    //4.3) 迭代器
    val iter = arr2.iterator
    while(iter.hasNext){
      println(iter.next())
    }

    println("=========foreach===========")
    //4.4) foreach
    arr2.foreach((elem: Int) => println(elem))
    arr2.foreach(elem => println(elem))
    arr2.foreach( println )

    //4.5) 第五种
    println(arr2.mkString(" - "))

    //5  添加元素   操作集合时，推荐  不可变集合用符号来操作；可变集合用方法来操作    （其实符号的底层也是方法）
    val newArr = arr2.:+(73)      //   :+
    println(arr2.mkString(","))
    println(newArr.mkString(","))

    val newArr2 = newArr.+:(11)   //  +:
    println(newArr2.mkString(","))

    val newArr3 = newArr2 :+ 15
    val newArr4 = 42 +: 10 +: newArr3

    println(newArr3.mkString(","))
    println(newArr4.mkString(","))


  }

}
