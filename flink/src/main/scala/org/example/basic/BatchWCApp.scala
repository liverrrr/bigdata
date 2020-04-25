package org.example.basic

import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.api.scala._

/**
 * Flink批程序编写流程
 * 无需触发
 */
object BatchWCApp {

  def main(args: Array[String]): Unit = {
    val env = ExecutionEnvironment.getExecutionEnvironment

    val input = env.readTextFile("flink-train-scala/input/wc.txt")

    val result = input.flatMap(_.split(","))
      .map((_, 1))
      .groupBy(0).sum(1)

    result.print()

  }

}
