package org.example.scala.class04

import java.io.File

import scala.io.Source
import ImplicitAspect.manToSuperMan
import ImplicitAspect.fileToRichFile

/**
  * 隐式转换
  * 目的：增强与扩展
  * 三大类型：隐式参数/隐式类型转换/隐式类
  * 常用还是前两个
  */
object ImplicitApp {

  /**
    * 隐式类
    * 功能十分强悍，但很难理解
    * implicit修饰类之后使得类的属性类型也能直接调用类的方法
    * 但是隐式类只允许主构造器只有一个
    */
  implicit class calculation(x: Int) {

    def add(y: Int) = x + y

  }

  /**
    * 隐式参数
    * 在方法和函数的参数使用implicit 编译器会在上下文中寻找类型一致的implicit修饰的变量
    * 但是如果出现两个或以上的类型一致的implicit修饰的变量，隐式参数所在的方法或函数会报错
    *
    */

  def introduction(implicit name: String): Unit = {
    println(s"hello $name..")
  }

  def introduction2(name: String)(implicit age: Int = 23) = {
    println(s"My name is $name,I am $age")
  }


  def main(args: Array[String]): Unit = {
    /**
      * 隐式类型转化 自动完成转化 A=>B
      * A既能调用本类的方法还能调用B类的方法
      *
      * 格式：
      * implicit def AToB(A):B=new B(...)
      * 例子：
      * implicit def manToSuperMan(man:Man):SuperMan=new SuperMan(man.name)
      */
    val man = new Man("张三")
    man.fly()
    man.say()

    val file = new File("D:\\若泽大数据\\G7-03\\data\\input\\wc.txt")
    println(file.read())

    println("------------------------------------------------------------------------------------------")

    /**
      * 隐式参数
      * 在调用方法/函数时可以忽略隐式参数传入
      */
    implicit val name = "ruozedata"
    implicit val age = 18
    introduction
    introduction2(name)

    println("------------------------------------------------------------------------------------------")

    /**
      * 隐式calculation类使得属性类型Int也具有add方法
      */
    println(2.add(4))
  }

}


class Man(val name: String) {
  def say() = {
    println("name=" + name)
  }
}

class SuperMan(val name: String) {
  def fly(): Unit = {
    println(s"$name can fly...")
  }
}

class RichFile(file: File) {
  def read(): String = {
    Source.fromFile(file.getPath).mkString
  }
}


