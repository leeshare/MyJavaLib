package chapter02

/**
 *    + 字符串正常拼接
 *    * 用于将一个字符串复制多次并拼接
 *    % printf 整型，通过%d传值；字符串，通过%s传值
 *    $ 字符串模板
 *    三引号
 */
object Test04_String {

  def main(args: Array[String]): Unit = {
    val name: String = "alice"
    val age: Int = 18
    // +
    println(age + "岁的" + name + "去北京")
    // *
    println(name * 3)
    // %
    printf("%d岁的%s去北京", age, name)
    println()
    // $    s + "" 表示模板字符串
    println(s"${age}岁的${name}去北京")

    val num: Double = 2.3456
    println(f"The num is ${num}%2.2f")   //格式化模板字符串
    println(raw"The num is ${num}%2.2f")
    //三引号 表示字符串，保持多行字符串的原格式输出
    val sql =
      s"""
         |select *
         |from student
         |where name = ${name}
         |and age > ${age}
     """.stripMargin
    println(sql)



}

}
