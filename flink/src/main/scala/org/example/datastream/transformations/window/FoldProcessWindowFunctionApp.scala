package org.example.datastream.transformations.window

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

object FoldProcessWindowFunctionApp {

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    env.socketTextStream("localhost", 7778)
      .map(str => {
        val splits = str.split(",")
        (splits(0), splits(1).toInt)
      })
      .keyBy(_._1)
      .timeWindow(Time.seconds(5))
      .fold(
        ("", 0, 0),
        (acc: (String, Int, Int), ele: (String, Int)) => {
          (ele._1, acc._2 + ele._2, acc._3 + 1)
        },
        (
          key: String,
          window: TimeWindow,
          counts: Iterable[(String, Int, Int)],
          out: Collector[(String, Long, Int)]
        ) => {
          val next = counts.iterator.next()
          out.collect((key,window.getStart,next._3))
        }
      )
        .print()
    env.execute(this.getClass.getSimpleName)
  }

}
