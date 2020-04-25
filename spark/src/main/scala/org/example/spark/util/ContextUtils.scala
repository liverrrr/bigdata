package org.example.spark.util

import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

object ContextUtils {

  def getContext(appName:String,master:String="local[2]"):SparkContext={
    val conf = new SparkConf().setMaster(master).setAppName(appName)
    new SparkContext(conf)
  }

  def getStreamingContext(appName:String,second:Int,master:String="local[2]"): StreamingContext ={
    val conf = new SparkConf().setMaster(master).setAppName(appName)
    new StreamingContext(conf,Seconds(second))
  }

}
