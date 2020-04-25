package org.example.datastream.transformations.window

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

object ReduceProcessWindowFunctionApp {

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    env.setParallelism(1)

    env.socketTextStream("localhost",7777)
        .map(ele =>{
          val splits = ele.split(",")
          (splits(0),splits(1).toInt)
        })
        .keyBy(_._1)
        .timeWindow(Time.seconds(5))
        .reduce((x: (String, Int), y: (String, Int)) =>{if (x._2 > y._2) (x._1,x._2) else (x._1,y._2)}, new MyProcessWindowFunction)
        .print()

    env.execute(this.getClass.getSimpleName)
  }

  class MyProcessWindowFunction extends ProcessWindowFunction[(String, Int),(String,Int),String,TimeWindow]{
    override def process(key: String, context: Context, elements: Iterable[(String, Int)], out: Collector[(String, Int)]): Unit = {
      val tuple = elements.iterator.next()
      out.collect(tuple)
    }
  }

}
