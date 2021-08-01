package chapter07

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
 * 可变数组
 */
object Test02_ArrayBuffer {
  def main(args: Array[String]): Unit = {
    // 1 创建可变数组
    val arr1: ArrayBuffer[Int] = new ArrayBuffer[Int]()  //默认分16个位置
    val arr2 = ArrayBuffer(23, 55, 91)

    println(arr1.mkString(", "))
    println(arr2)   //默认调用 toString()
    println(arr2.toString())

    //2 访问元素
    println(arr2(1))
    arr2(1) = 39
    println(arr2(1))

    //3 向数组加元素
    //3.1 用不可变数组的方法 加元素
    val newArr1 = arr1 :+ 15
    println(newArr1(0))
    println(newArr1 == arr1)

    //3.2 用可变数组的方法 加元素
    arr1 += 19
    println(arr1(0))
    val newArr2 = arr1 += 20  //尽量不要这样操作，因为是同一个对象
    println(newArr2 == arr1)  //true
    18 +=: arr1
    println(arr1)
    println(newArr2)  //这个数组自动变化 因为和 arr1 指向同一个地址

    arr1.append(30)
    arr1.prepend(10)
    arr1.insert(1, 15, 16)  //在 1 位置 加了2个元素
    println(arr1)

    arr1.insertAll(2, newArr1)
    arr1.prependAll(newArr2)
    println(arr1)

    //6  删除元素
    arr1.remove(3)
    println(arr1)

    arr1.remove(0, 10)
    println(arr1)

    arr1 -= 30
    println(arr1)

    //7 可变数组 转换为 不可变数组
    val arr: ArrayBuffer[Int] = ArrayBuffer(23, 56, 98)
    val newArr: Array[Int] = arr.toArray
    println(newArr.mkString("-"))

    //7 不可变数组 转换为 可变数组
    val buffer: mutable.Buffer[Int] = newArr.toBuffer
    println(buffer)
    println(newArr.mkString(", "))

}

}
