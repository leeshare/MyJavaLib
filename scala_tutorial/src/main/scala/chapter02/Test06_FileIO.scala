package chapter02

import java.io.PrintWriter

import scala.io.Source

object Test06_FileIO {
  def main(args: Array[String]): Unit = {

    //这个路径 对应的是 项目根目录下的 src下；而不是项目 当前 Module的目录下。
    Source.fromFile("src/main/resources/test.txt").foreach(print)

    val writer = new PrintWriter(new java.io.File("src/main/resources/output.txt"))
    writer.write("hello scala from java writer")
    writer.close()
}

}
