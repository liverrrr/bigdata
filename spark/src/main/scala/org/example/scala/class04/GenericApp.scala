package org.example.scala.class04

import org.example.scala.class04.CupEnum.CupEnum

/**
  * 泛型主要是解耦与约束
  */
object GenericApp {

  def main(args: Array[String]): Unit = {
    val mm1 = new MM[Int, CupEnum, Int](10, CupEnum.B, 175)
    val mm2 = new MM[Int, CupEnum, Int](5, CupEnum.F, 157)
    val mm3 = new MM[Int, CupEnum, Int](8, CupEnum.C, 165)

    println(mm1)
    println(mm2)
    println(mm3)

    println("------------------------------------------------------------------")

    /**
      * 子类=>父类  逆变
      * class Inverter[-Father]
      */
    val inverter: Inverter[Father] = new Inverter[Person]

    /**
      * 父类=>子类  斜变
      * class Oblique[+Father]
      */
    val Oblique: Oblique[Father] = new Oblique[Son]
  }
}

class Inverter[-Father]
class Oblique[+Father]

class Person
class Father extends Person
class Son extends Father

class MM[A, B, C](faceValue: A, cup: B, height: C){
  override def toString: String = faceValue+"\t"+cup+"\t"+height
}

/**
  * scala枚举的使用
  */
object CupEnum extends Enumeration {
  type CupEnum = Value
  val A, B, C, D, E, F, G = Value
}