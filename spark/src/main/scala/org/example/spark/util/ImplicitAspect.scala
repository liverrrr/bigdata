package org.example.spark.util

import org.apache.spark.rdd.RDD


object ImplicitAspect {
  implicit def RDD2RichRDD[T](rdd: RDD[T]):RichRDD[T]=new RichRDD[T](rdd)
}

class RichRDD[T](rdd:RDD[T]){

  def printInfo(isRepeat:Int=0): Unit ={
    if(isRepeat==0){
      rdd.collect().foreach(println)
      println("~~~~~~~~~~~~~~~~~~~")
    }
  }
}

