package chapter07

object Test08_ImmutableMap {
  def main(args: Array[String]): Unit = {
    //1 create
    val map1: Map[String, Int] = Map[String, Int]("a" -> 13, "hello" -> 25, "scala" -> 0)
    println(map1)
    println(map1.getClass)

    //2 遍历
    map1.foreach(println)
    map1.foreach((kv: (String, Int)) => println(kv))

    //3
    for(key <- map1.keys){
      println(s"${key}-----> ${map1.get(key)}")
    }

    //4
    println(map1.get("a").get)
    println(map1.get("a"))
    println(map1.getOrElse("c", 0))

    println(map1("a"))
}

}
