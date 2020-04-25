package org.example.scala.class02

import scala.collection.mutable.ArrayBuffer

object ArrayApp {
  def main(args: Array[String]): Unit = {
    /**
      * 定长数组是Array()
      * 变长数组是ArrayBuffer()
      */
    val array = Array("ruoze", "jepson", "xingxing")
    val buffer = ArrayBuffer[Int]()

    buffer += 1
    buffer.insert(1, 2)
    buffer.size
    buffer.remove(1)
    buffer ++= Array(11, 12, 13)

    /**
      * mkString 以指定的分隔符拼接
      */
    array.mkString(",")
    array.mkString
    array.mkString("[", "$$$", "]")

    /**
      * 反转数组
      */
    array.reverse
    for (i <- array.length - 1 to 0 by -1) {
      println(array(i))
    }

    val a = Array(1, 2, 3, 4, 5, 6, 7, 8)

    /**
      * map 一一映射
      */
    a.map(x => x * 2)
    a.map(_ * 2)

    /**
      * filter 过滤 true
      */
    a.filter(_ > 5)

    /**
      * reduce 两两相加 默认从左边开始
      * reduceLeft 从左边开始两两相加
      * reduceRight 从右边开始两两相加
      */
    a.reduce(_ + _)
    a.reduce(_ - _)
    a.reduceLeft(_ + _)
    a.reduceLeft(_ - _)
    a.reduceRight(_ + _)
    a.reduceRight(_ - _)

    a.reduceRight((a, b) => {
      println("a:" + a + " b:" + b + " a+b=" + (a - b))
      a - b
    })

    /**
      * zip 两个数组依据下标匹配 得到数组长度由两个中最短决定
      */
    val numZip = Array(1, 2, 3, 4)
    val strZip = Array("A", "B", "C", "D", "E")
    numZip.zip(strZip)


    /**
      * flatMap = flatten + map
      */
    val numArray = Array(Array(1, 2), Array(3, 4), Array(5, 6))
    numArray.flatMap(_.map(_ * 2))
    numArray.flatMap(_.map(_ * 2).filter(_ > 5))
    numArray.flatten.map(_ * 2).filter(_ > 5)

    /**
      * find 找到符合条件的第一个
      */
    numZip.find(_ > 3)

    /**
      * take 从左截取n个
      * takeRight 从右截取n个
      */
    numArray.flatten.take(3)
    numArray.flatten.takeRight(3)

    /**
      * group by分组
      */
    val numGroup=Array(("a",100),("b",10),("c",15),("b",140))
    numGroup.groupBy(_._1)

    /**
      * sortby 排序
      */
    numGroup.sortBy(_._2)
    numGroup.sortBy(-_._2) //倒序

    /**
      * scala 实现wc例子
      */
    val wc=Array("hello,hello,world","ruoze,ruoze")
    wc.flatMap(_.split(","))
      .map((_,1))
      .groupBy(_._1)
      .mapValues(_.map(_._2).sum)
      .toList.sortBy(-_._2)


  }
}
