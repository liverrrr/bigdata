package org.example.datastream.transformations.window

import org.apache.flink.api.common.functions.AggregateFunction
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time

/**
 * 取window内第二个字段的平均值
 */
object WindowAggregateFunctionApp {

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    env.socketTextStream("localhost",7777)
      .map(str =>{
        val splits = str.split(",")
        (splits(0),splits(1).toInt)
      })
      .keyBy(_._1)
      .timeWindow(Time.seconds(5))
      .aggregate(new AverageAggregate)
        .print()

    env.execute(this.getClass.getSimpleName)
  }

  class AverageAggregate extends AggregateFunction[(String, Int), (Int, Int), Double] {
    override def createAccumulator(): (Int, Int) = (0, 0)

    override def add(value: (String, Int), accumulator: (Int, Int)): (Int, Int) = (accumulator._1 + value._2, accumulator._2 + 1)

    override def getResult(accumulator: (Int, Int)): Double = accumulator._1 / accumulator._2

    override def merge(a: (Int, Int), b: (Int, Int)): (Int, Int) = (a._1 + b._1 , a._2 + b._2)
  }

}
