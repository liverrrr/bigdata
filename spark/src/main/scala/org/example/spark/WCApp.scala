package org.example.spark

import org.apache.spark.{SparkConf, SparkContext}

object WCApp {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[2]").setAppName("WCApp")
    val sc = new SparkContext(conf)

    sc.textFile("data/input/wc.txt")
      .flatMap(_.split(","))
      .map((_,1))
      .reduceByKey(_+_)
      .collect()
  }

}
