package org.example.spark.streaming

import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.{HasOffsetRanges, KafkaUtils}
import org.example.spark.util.ContextUtils

object StreamDirectApp {

  def main(args: Array[String]): Unit = {
    val ssc = ContextUtils.getStreamingContext(this.getClass.getSimpleName, 5)

    val groupId = "ruozedata_groupId"
    val topic = "sparkstream"

    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> "hadoop001:9092,hadoop001:9093,hadoop001:9094",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> groupId,
      "auto.offset.reset" -> "earliest",
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )

    val topics = Array(topic)

    //    val fromOffsets = RedisOffsetManager.obtainOffsets(topics(0) + "_" + groupId)
    val fromOffsets = MySQLOffsetManager.obtainOffsets(topics(0) + "_" + groupId)
    val stream = KafkaUtils.createDirectStream[String, String](
      ssc,
      PreferConsistent,
      Subscribe[String, String](topics, kafkaParams, fromOffsets)
    )


    stream.foreachRDD { rdd =>
      println("~~~~~~~~~~~~~~分区数：" + rdd.partitions.length)
      val offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges

      //业务逻辑

      /**
        * 外部存储offset要设计好数据结构
        * 数据 + offset => 保证一起成功否则一起失败 幂等性(精确一次)
        */
      //      RedisOffsetManager.storeOffsets(offsetRanges, topics(0) + "_" + groupId)
      MySQLOffsetManager.storeOffsets(offsetRanges, topics(0) + "_" + groupId)

      /**
        * offset提交存放kafka，但是不具有幂等性，一旦作业挂掉但是未提交，就会产生消费重复
        *  stream.asInstanceOf[CanCommitOffsets].commitAsync(offsetRanges)
        * 详见http://spark.apache.org/docs/latest/streaming-kafka-0-10-integration.html#kafka-itself
        */


    }

    ssc.start()
    ssc.awaitTermination()
  }

}
