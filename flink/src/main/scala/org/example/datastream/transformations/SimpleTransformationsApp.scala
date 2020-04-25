package org.example.datastream.transformations

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._
import org.example.domain.Domain.Access

object SimpleTransformationsApp {

  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val stream = env.readTextFile("input/access.log")
      .map(ele => {
        val splits = ele.split(",")
        Access(splits(0), splits(1), splits(2).toInt)
      })

    /**
     * keyBy:分组
     * sum:聚合
     *
     * stream.keyBy("domain").sum("flow").print("sum = ")
     */

    /**
     * reduce:相邻的数据做操作，例如两两相加
     *
     * stream.keyBy("domain").reduce((x, y) => {
     * Access(x.time, x.domain, x.flow + y.flow)
     * }).print()
     */

    /**
     * fold:只对分组之后的数据起作用，将事先指定好的数据与分组之后的数据操作
     *
     *  stream.keyBy("domain")
     * .fold(100)((num, x) => num + x.flow)
     * .print()
     */

    /**
     * split:依据定义好的函数将数据流分成多个数据流
     * select:只跟在split后操作，获得指定的数据流
     *
     * val split = stream.split(x => {
     *  if (x.flow > 5000) {
     *    List("vip")
     *  } else {
     *    List("normal")
     *  }
     * })
     *
     * split.select("vip").print("vip")
     * split.select("normal").print("normal")
     */

    /**
     * union:将类型相同的数据流合并在一起
     * connect:将类型不同的数据流合并在一起
     *
     * val otherStream = env.readTextFile("input/access.log")
     * .map(ele => {
     *  val splits = ele.split(",")
     *  Access(splits(0), splits(1), splits(2).toInt)
     * })
     * stream.union(otherStream).print()
     *
     * val newStream = env.generateSequence(1, 100)
     * stream.connect(newStream)
     * .map(x => x, y => y)
     * .print()
     */


    env.execute(this.getClass.getSimpleName)
  }

}
