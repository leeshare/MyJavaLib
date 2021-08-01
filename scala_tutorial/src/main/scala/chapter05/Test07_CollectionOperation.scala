package chapter05

object Test07_CollectionOperation {
  def main(args: Array[String]): Unit = {
    //对数组进行处理，将操作抽象出来，处理之后的结果，返回一个新的数组

    val arr: Array[Int] = Array(12, 45, 75, 98)
    def arrayOperation(arr: Array[Int], op: Int=>Int): Array[Int] = {
      for(elec <- arr) yield op(elec)
    }

    //定义一个加一操作
    def addOne(elec: Int): Int = {
      elec + 1
    }

    //调用
    val newArray: Array[Int] = arrayOperation(arr, addOne _)
    println(newArray.mkString(","))

    val newArray2 = arrayOperation(arr, _ * 2)
    println(newArray2.mkString(","))




  }

}
