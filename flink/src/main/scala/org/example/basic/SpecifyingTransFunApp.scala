package org.example.basic

import org.apache.flink.api.common.functions.{RichFilterFunction, RichMapFunction}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.scala._
import org.example.domain.Domain.Access

object SpecifyingTransFunApp {

  def main(args: Array[String]): Unit = {
    
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    /**
     * Lambda Functions
     */
    env.readTextFile("input/access.log")
        .map(x => {
          val splits = x.split(",")
          (splits(0).toLong,splits(1),splits(2).toInt)
        })
        .filter(_._3 > 5000)
        .print().setParallelism(1)

    /**
     * Rich functions
     */
    env.readTextFile("input/access.log")
        .map(new RichMapFunction[String,Access] {
          //setup methods
          override def open(parameters: Configuration): Unit = {
            println("~~~~~~~~~open~~~~~~~~~~~~")
            super.open(parameters)
          }

          override def map(input: String): Access = {
            val splits = input.split(",")
            Access(splits(0),splits(1),splits(2).toInt)
          }

          //teardown methods
          override def close(): Unit = super.close()
        })
      .filter(new MyFilterFun)
      .print().setParallelism(1)


    env.execute(this.getClass.getSimpleName)
  }

  class MyFilterFun extends RichFilterFunction[Access]{
    override def filter(input: Access): Boolean = {
      input.flow > 5000
    }
  }

}
