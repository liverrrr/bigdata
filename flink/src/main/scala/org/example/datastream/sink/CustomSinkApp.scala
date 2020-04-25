package org.example.datastream.sink

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.redis.RedisSink
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig
import org.example.domain.Domain.Access

object CustomSinkApp {

  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.readTextFile("input/access.log")
      .map(x => {
        val splits = x.split(",")
        val time = splits(0)
        val domain = splits(1)
        val flow = splits(2).toInt
        Access(time, domain, flow)
      })
      .addSink(new MySQLRichSink)

    val conf = new FlinkJedisPoolConfig.Builder().setHost("localhost").build()
    env.readTextFile("input/access.log")
      .map(x => {
        val splits = x.split(",")
        val domain = splits(1)
        val flow = splits(2).toInt
        (domain, flow)
      }).keyBy(0).sum(1)
      .addSink(new RedisSink[(String, Int)](conf, new RedisSinkMapper))


    env.execute(this.getClass.getSimpleName)

  }

}
