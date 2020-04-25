package org.example.scala.class02

object ApplyApp {

  def main(args: Array[String]): Unit = {
//    ApplyTest.test()
//    val test = new ApplyTest
//    test.apply()
    val apply = ApplyTest()  //调用得是object的apply
    val test = new ApplyTest
    test()  //调用的是class的apply
  }

  /**
    * object是class的伴生对象
    * class是object的伴生类
    * 类名()=>object apply
    * 对象()=>class apply
    */
  object ApplyTest{
    println("进入object ApplyTest")
    val name="name"

    def test()={
      println("test")
    }

    def apply(): Unit ={
      println("object apply")
      new ApplyTest
    }
    println("退出object ApplyTest")
  }

  class ApplyTest{
    def apply(): Unit ={
      println("class apply")
    }
  }
}
