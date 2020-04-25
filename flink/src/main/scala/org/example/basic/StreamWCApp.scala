package org.example.basic

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.api.scala._

/**
 * Flinka流程序编写流程
 */
object StreamWCApp {

  def main(args: Array[String]): Unit = {
    //1. Obtain an execution environment
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    //2. Load/create the initial data
    val ds = env.socketTextStream("localhost",7777)

    //3. Specify transformations on this data
    val result = ds.flatMap(_.split(","))
      .map((_, 1))
      .keyBy(0)
      .sum(1)

    //4. Specify where to put the results of your computations
    result.print()

    //5. Trigger the program execution
    env.execute(this.getClass.getSimpleName)
  }

}
