package org.example.spark.rdd.example02

import org.example.spark.util.ContextUtils
import org.example.spark.util.ImplicitAspect._

object InterviewApp02 {

  def main(args: Array[String]): Unit = {
    val sc = ContextUtils.getContext(this.getClass.getSimpleName)

    val input = sc.parallelize(List(
      "1000000,一起看|电视剧|军旅|士兵突击,1,0",
      "1000000,一起看|电视剧|军旅|士兵突击,1,1",
      "1000001,一起看|电视剧|军旅|我的团长我的团,1,1"
    ))
    input.flatMap(x => {
      val splits = x.split(",")
      val userId = splits(0)
      val show = splits(2).toInt
      val click = splits(3).toInt
      val words = splits(1).split("\\|")

      words.map(x => ((userId, x), (show, click)))
    }).reduceByKey((x, y) => (x._1 + y._1, x._2 + y._2))
      .map(x => (x._1._1, x._1._2, x._2._1, x._2._2))
      .sortBy(_._1)
      .printInfo()

    sc.stop()
  }

}
