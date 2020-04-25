package org.example.datastream.transformations.window

import org.apache.flink.api.common.functions.AggregateFunction
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector


object AggregationProcessWindowFunctionApp {

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    env.socketTextStream("localhost", 7777)
      .map(str => {
        val splits = str.split(",")
        (splits(0), splits(1).toInt)
      })
      .keyBy(_._1)
      .timeWindow(Time.seconds(5))
        .aggregate(new MyAggregateFunction, new MyProcessWindowFunction)
        .print()

    env.execute(this.getClass.getSimpleName)
  }

  class  MyAggregateFunction extends AggregateFunction[(String,Int),(Int,Int),Double]{
    override def createAccumulator(): (Int, Int) = (0,0)

    override def add(value: (String, Int), accumulator: (Int, Int)): (Int, Int) = (accumulator._1 + value._2, accumulator._2 + 1)

    override def getResult(accumulator: (Int, Int)): Double = accumulator._1 / accumulator._2

    override def merge(a: (Int, Int), b: (Int, Int)): (Int, Int) = (a._1 + b._1, a._2 + b._2)
  }

  class MyProcessWindowFunction extends ProcessWindowFunction[Double, (String,Double), String, TimeWindow] {
    override def process(key: String, context: Context, elements: Iterable[Double], out: Collector[(String, Double)]): Unit = {
      val next = elements.iterator.next()
      out.collect((key,next))
    }
  }

}
