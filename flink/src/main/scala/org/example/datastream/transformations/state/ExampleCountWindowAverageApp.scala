package org.example.datastream.transformations.state

import org.apache.flink.api.common.functions.RichFlatMapFunction
import org.apache.flink.api.common.state.{ValueState, ValueStateDescriptor}
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.api.scala._
import org.apache.flink.configuration.Configuration
import org.apache.flink.util.Collector

/**
 * 官方ValueState使用举例
 */
object ExampleCountWindowAverageApp {

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    env.fromCollection(List(
      (1L, 3L),
      (1L, 5L),
      (1L, 7L),
      (1L, 4L),
      (1L, 2L))
    ).keyBy(_._1)
      .flatMap(new CountWindowAverage)
      .print().setParallelism(1)

    env.execute(this.getClass.getSimpleName)
  }

  class CountWindowAverage extends RichFlatMapFunction[(Long, Long), (Long, Float)] {

    //第一个值是个数，后面是具体累加值
    private var sum: ValueState[(Long, Long)] = _

    override def open(parameters: Configuration): Unit = {
      sum = getRuntimeContext.getState(
        new ValueStateDescriptor[(Long, Long)]("average", createTypeInformation[(Long, Long)])
      )
    }

    override def flatMap(input: (Long, Long), out: Collector[(Long, Float)]): Unit = {
      //获取当前状态的值，如果从来没使用过则赋予(0L,0L)
      val currentSum = if (sum.value() != null) {
        sum.value()
      } else {
        (0L, 0L)
      }

      val newSum = (currentSum._1 + 1, input._2 + currentSum._2)
      sum.update(newSum)

      //个数达到2就做一次取平均值
      if (newSum._1 >= 2) {
        out.collect((input._1, newSum._2 / newSum._1.asInstanceOf[Float]))
        sum.clear()
      }
    }
  }

}
