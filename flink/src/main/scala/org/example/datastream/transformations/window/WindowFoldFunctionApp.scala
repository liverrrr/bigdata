package org.example.datastream.transformations.window

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time

/**
 * 将所有输入Long值附加到最初为空的String上。
 */
object WindowFoldFunctionApp {

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    env.socketTextStream("localhost",7777)
        .map(str => {
          val splits = str.split(",")
          (splits(0),splits(1).toLong)
        }).keyBy(_._1)
        .timeWindow(Time.seconds(3))
        .fold(""){(acc,v) => acc + v._2}
        .print()

    env.execute(this.getClass.getSimpleName)
  }

}
