package chapter05

object Test05_Lambda {

  def main(args: Array[String]): Unit = {

    //val stringToUnit: String => Unit = (name: String) => { println(name) }    //返回类型是 String => Unit  这叫函数类型
    val fun = (name: String) => { println(name) }
    fun("lee")

    //lambda表达式 作为一个参数传给另一个函数
    def f(func: String => Unit ): Unit = {
      func("lee")
    }
    //不是把 要干的什么事传进来（只传入数据即可） ————> 而是把操作传进来。数据定死的。

    f(fun)
    f( (name: String) => { println(name) })

    def f2(func: String => Unit, name: String): Unit = {
      func(name)
    }

    f2(fun, "hello")


    //匿名函数的简化
    //1 参数类型可省略   2 括号可省略     3 大括号可省略
    f(name => println(name) )
    //4 只有一个参数，则参数名省略 用 _ 代替
    f( println(_) )
    //5
    f( println )


    f2( println, "China " )

    println("=============二元函数===========")

    def dualFunctionOneAndTwo(fun: (Int, Int) => Int): Int = {
      fun(1, 2)
    }

    val add = (a: Int, b: Int) => a + b
    val minus = (a: Int, b: Int) => a - b

    println(dualFunctionOneAndTwo(add))
    println(dualFunctionOneAndTwo(minus))

    println(dualFunctionOneAndTwo( (a, b) => a + b ))
    println(dualFunctionOneAndTwo( _ + _ ))
    println(dualFunctionOneAndTwo( _ - _ ))
    println(dualFunctionOneAndTwo( (a, b) => b - a ))


}

}
