package org.example.scala.class03

import scala.util.Random

object MatchApp {

  def main(args: Array[String]): Unit = {

    /**
      * case class匹配
      */
    val caseclasses = Array(CheckTimeOutTask, HeartBeat(3000), SubmitTask("100", "task100"))

    caseclasses(Random.nextInt(caseclasses.length)) match {
      case CheckTimeOutTask => println("CheckTimeOutTask")
      case HeartBeat(time) => println("HeartBeat")
      case SubmitTask(id, name) => println("SubmitTask")
    }


  }

  private def matchContent = {
    /**
      * 内容匹配
      */
    val contents = Array("Aoi Sola", "YuiHatano", "Akiho Yoshizawa")
    val name = contents(Random.nextInt(contents.length))
    name match {
      case "Aoi Sola" => println("苍老师")
      case "YuiHatano" => println("波老师")
      case _ => println("不认识")
    }
  }

  /**
    * 类型匹配
    */
  def matchType(obj: Any) = {
    obj match {
      case x: Int => println("this is Int..")
      case y: String => println("this is String ..")
      case z: Map[_, _] => println("this is Map ..")
      case _ => println("not know ..")
    }
  }

  /**
    * 集合匹配
    */
  def matchCollection(list: List[String]) = {
    list match {
      case "ruoze" :: Nil => println("Hello: ruoze") // 只能匹配只有ruoze一个元素
      case x :: y :: Nil => println(s"Hi: $x , $y") // 能匹配集合中有两个元素的
      case "jepson" :: tail => println("Hi:jepson and others") // 匹配jepson开头的
      case _ => println("......")
    }
  }


}

case class SubmitTask(id: String, name: String)

case class HeartBeat(time: Long)

case object CheckTimeOutTask