package org.example.basic

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._
import org.example.domain.Domain.WC

/**
 * Flink如何指定key
 */
object SpecifyKeyApp {

  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val input = env.readTextFile("input/wc.txt")

    /**
     * Define keys for Tuples
     */
    val result = input.flatMap(_.split(",")).map((_, 1)).keyBy(0).sum(1)

    /**
     * Define keys using Field Expressions
     */
    val result2 = input.flatMap(_.split(","))
      .map(ele => {
        WC(ele, 1)
      })
      .keyBy("word")
      .sum("count")

    /**
     * Define keys using Key Selector Functions
     */
    val result3 = input.flatMap(_.split(","))
      .map(x => {
        WC(x, 1)
      })
      .keyBy(_.word)
      .sum("count")

    result2.print().setParallelism(1)

    env.execute(this.getClass.getSimpleName)
  }

}
