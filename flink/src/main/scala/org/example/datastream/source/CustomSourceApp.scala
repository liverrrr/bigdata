package org.example.datastream.source

import java.util.Properties

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.streaming.util.serialization.SimpleStringSchema

object CustomSourceApp {

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    //自定义生成数据，并行度不能被设置超过1
    val ds = env.addSource(new MyNonParallelSource)

    //自定义生成数据，并行度能被设置大于1
    val ds2 = env.addSource(new MyParallelSource)

    //自定义从MySQL中取出数据，支持并行度大于1
    val ds3 = env.addSource(new MySQLSource)

    val properties = new Properties()
    properties.setProperty("bootstrap.servers", "localhost:9092")
    properties.setProperty("group.id", "test")
    properties.setProperty("request.timeout.ms","90000")
    val myConsumer = new FlinkKafkaConsumer[String]("ruozedata", new SimpleStringSchema(), properties)
    myConsumer.setStartFromEarliest()

    env.addSource(myConsumer).print()

    env.execute(this.getClass.getSimpleName)
  }

}
