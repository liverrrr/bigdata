package org.example.scala.class02

/**
  * 抽象类:
  * 1） 类中有一个或者多个方法/属性的定义，没有实现
  * 2） 抽象类是不能直接new
  * 3） 使用时要有完全实现了抽象方法/属性的子类才行
  * 4） 子类重写父类方法或者属性时不一定需要override
  */
object AbstractClassApp {

  def main(args: Array[String]): Unit = {
    val people = new People
    people.speak
    println(people.sum(1))
  }

}

abstract class action {
  def speak

  val word: String
  val a=1
  def sum(a:Int)=a+1
}

class People extends action {
  def speak: Unit = {
    println(word)
  }

  override val word: String = "hello"
}