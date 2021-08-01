package chapter05

object Test08_Practise {
  def main(args: Array[String]): Unit = {

    val fun = (a: Int, b: String, c: Char) => {
      if (a == 0 && b == "" && c == '0') {
        false
      }else {
        true
      }
    }

    println(fun(0, "", '0'))
    println(fun(20, "", '0'))

    def func(a: Int): String=>(Char=>Boolean) = {
      def f1(b: String): Char=>Boolean = {
        def f2(c: Char): Boolean = {
          if(a == 0 && b == "" && c == '0') false else true
        }
        f2
      }
      f1
    }

    println(func(0)("")('0'))
    println(func(20)("")('0'))

    def func1(i: Int) = { (s: String) =>
      (c: Char) => {
        if (i == 0 && s == "" && c == '0') false else true
      }
    }
    println(func1(0)("")('0'))
    println(func1(20)("")('0'))

    //柯里化  闭包
    def func2(i: Int)(s: String)(c: Char): Boolean = {
      if (i == 0 && s == "" && c == '0') false else true
    }
    println(func1(0)("")('0'))
    println(func1(20)("")('0'))

  }

}
