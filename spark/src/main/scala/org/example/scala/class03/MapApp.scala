package org.example.scala.class03

import scala.collection.mutable.Map

/**
  * map 可变  不可变
  * key->value
  */
object MapApp {

  def main(args: Array[String]): Unit = {
    val map = Map("zhangsan" -> 13, "lisi" -> 14, "wangwu" -> 20)

    //map 第一种遍历
    for ((k, v) <- map) {
      println(k + " -> " + v)
    }

    //map 第二种遍历
    for (k <- map.keySet) {
      println(k + " -> " + map.getOrElse(k, -99))
    }

    //map 第三种遍历
    for (v <- map.values) {
      println(v)
    }


    /**
      * 如果想使用有顺序的Map：scala.collection.immutable.SortedMap和scala.collection.mutable.LinkedHashMap
      */

    /**
      * get()返回Option
      * Option有两个子类Some和None
      * Some代表有值
      * None代表没值
      */
    val maybeInt = map.get("dengdi")
  }
}
