package org.example.basic

import org.apache.flink.api.common.accumulators.IntCounter
import org.apache.flink.api.common.functions.{RichFilterFunction, RichMapFunction}
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.api.scala._
import org.apache.flink.configuration.Configuration
import org.example.domain.Domain.Access

object UseAccumulatorsApp {

  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment

    env.readTextFile("input/access.txt")
      .map(new RichMapFunction[String, Access] {
        private val total = new IntCounter()
        private val wrong = new IntCounter()

        override def open(parameters: Configuration): Unit = {
          getRuntimeContext.addAccumulator("num_total", total)
          getRuntimeContext.addAccumulator("num_wrong", wrong)
        }

        override def close(): Unit = super.close()

        override def map(input: String): Access = {
          total.add(1)
          try {
            val splits = input.split(",")
            Access(splits(0), splits(1), splits(2).toInt)
          } catch {
            case e: Exception => {
              wrong.add(1)
              null
            }
          }
        }
      })

    val myJobExecutionResult = env.execute(this.getClass.getSimpleName)
    val resultTotal = myJobExecutionResult.getAccumulatorResult[Int]("num_total")
    val resultWrong = myJobExecutionResult.getAccumulatorResult[Int]("num_wrong")
    println("total num is " + resultTotal + ",wrong num is " + resultWrong)
  }

}
