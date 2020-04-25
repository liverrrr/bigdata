package org.example.spark.rdd.example02

import org.example.spark.util.ContextUtils
import org.example.spark.util.ImplicitAspect._

/**
  * a 2 1    a,5,8
  * a 3 7 =>
  * b 4 5    b,4,5
  */
object InterviewApp01 {

  def main(args: Array[String]): Unit = {
    val sc = ContextUtils.getContext(this.getClass.getSimpleName)

    val input = sc.parallelize(List("a 2 1","a 3 7","b 4 5"))
    input.map(x=>{
      val splits = x.split(" ")

      (splits(0),(splits(1).toInt,splits(2).toInt))
    }).reduceByKey((x,y)=>(x._1+y._1,x._2+y._2))
        .map(x=>(x._1,x._2._1,x._2._2))
        .printInfo()

    sc.stop()
  }

}
