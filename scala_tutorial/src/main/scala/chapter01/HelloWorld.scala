package chapter01

/**
 * object 关键字，声明一个单例对象（伴生对象）
 */
object HelloWorld {
  /**
   * main 方法：从外部可以直接调用执行的方法
   * def 方法名称（参数名称：参数类型）： 返回值类型 = { 方法体 }
   * scala泛型
   * @param args
   */
  def main(args: Array[String]): Unit = {
    println("hello world")
    //调用java类库
    System.out.println("hello scala from java")
  }

}
