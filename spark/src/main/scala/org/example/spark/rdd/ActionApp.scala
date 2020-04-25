package org.example.spark.rdd

import org.example.spark.util.ContextUtils

object ActionApp {

  def main(args: Array[String]): Unit = {
    val sc = ContextUtils.getContext(this.getClass.getSimpleName)

    val rdd = sc.parallelize(List(2,3,1,6,9,7,8))

    /**
      * collect只适合数据量小使用
      */
    rdd.collect()

    /**
      * 遍历每个RDD元素并进行操作
      */
    rdd.foreach(println)
    rdd.foreachPartition(println)

    /**
      * 通过二进制运算把rdd元素减少
      */
    rdd.reduce(_+_)

    /**
      * 计算rdd元素个数
      */
    rdd.count()

    /**
      * 拿取rdd元素的前n个
      */
    rdd.take(2)

    /**
      * 返回rdd元素的第一个
      * 底层调用还是take
      */
    rdd.first()

    /**
      * 返回rdd元素中最小的前n个
    */
    rdd.takeOrdered(3)

    /**
      * 返回rdd元素中最大的前n个
      * 底层还是调用的takeOrdered
      */
    rdd.top(3)

    /**
      * 计算key有多少个
      */
    val a = sc.parallelize(List(("a",30),("b",40),("a",10)))
    a.countByKey()


    sc.stop()
  }

}
