package org.example.scala.class03

import scala.collection.mutable.ListBuffer

/**
  * List 有序 可重复   不可变
  * ListBuffer 可变
  * Set  无序 不可重复 不可变
  * SetBuffer  可变
  */
object ListApp {

  def main(args: Array[String]): Unit = {
    val nums=List(1,2,3,4,5)
    println(sum(nums:_*))

    println(nums.contains(3))

    val a=new ListBuffer[Int]()
    a+=1
    a+=(2,3)
    a++List(4,5)
  }

  /**
    * Nil是个不可变的size为0的List
    *
    */
  def NewGetList() = {
    val a = 1 :: Nil //不可变的List(1)
    val b = (1, 2, 3) :: Nil //不可变的List((1,2,3))
  }

  /**
    * 传入数组来做递归求和
    * head取List的第一个数
    * tail取List的除第一个数以外的所有
   */
  def sum(nums: Int*): Int = {
    if (nums.isEmpty) {
      0
    }
    else {
      nums.head + sum(nums.tail: _*)
    }
  }

  /**
    * zip 跟数组的zip是一样的
    * zipWithIndex 添加index
    */
  def listZip():Unit={
    val a=List("A","B","c")
    val b=List(1,2,3,4)
    val zipResult = a.zip(b) //List((A,1), (B,2), (c,3))
    val withIndex = zipResult.zipWithIndex.zipWithIndex //List((((A,1),0),0), (((B,2),1),1), (((c,3),2),2))
  }


}
