package org.example.datastream.transformations

import org.apache.flink.api.common.functions.{RichFilterFunction, RichMapFunction}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._
import org.example.domain.Domain.Access

object RichTransformationsApp {

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    env.readTextFile("input/access.log")
      .map(new RichMapFunction[String, Access] {

        override def open(parameters: Configuration): Unit = {
          println("~~~~~~~~~open~~~~~~~~~")
          super.open(parameters)
        }

        override def close(): Unit = super.close()

        override def map(input: String): Access = {
          val splits = input.split(",")
          val time = splits(0)
          val domain = splits(1)
          val flow = splits(2).toInt
          Access(time, domain, flow)
        }
      }).keyBy("domain").sum("flow")
      .filter(new MyFilterFunction)
      .print().setParallelism(1)

    env.execute(this.getClass.getSimpleName)
  }

  class MyFilterFunction extends RichFilterFunction[Access] {

    override def open(parameters: Configuration): Unit = {
      println("~~~~~~~~filter open~~~~~~~~")
      super.open(parameters)
    }

    override def close(): Unit = super.close()

    override def filter(input: Access): Boolean = input.flow > 5000
  }

}
