package org.example.spark.streaming

import java.util.Properties

import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.{CanCommitOffsets, HasOffsetRanges, KafkaUtils}
import org.example.spark.util.ContextUtils

object Kafka2KafkaStreamingApp {

  def main(args: Array[String]): Unit = {

    val props = new Properties()
    props.put("bootstrap.servers", "hadoop001:9092,hadoop001:9093,hadoop001:9094")
    props.put("acks", "all")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

    val ssc = ContextUtils.getStreamingContext(this.getClass.getSimpleName, 5)

    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> "hadoop001:9092,hadoop001:9093,hadoop001:9094",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "use_a_separate_group_id_for_each_stream",
      "auto.offset.reset" -> "earliest",
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )

    val topics = Array("sparkstream")
    val stream = KafkaUtils.createDirectStream[String, String](
      ssc,
      PreferConsistent,
      Subscribe[String, String](topics, kafkaParams)
    )


    val kafkaSink = ssc.sparkContext.broadcast(KafkaSink(props))
    stream.foreachRDD { rdd =>
      if (rdd.count() == 0) {
        println("no data in current batch")
      }
      val offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges


      //      rdd.map(_.value()).flatMap(_.split(",")).map((_, 1)).reduceByKey(_ + _)
      //        .foreachPartition(partition=>{
      //          val producer = new KafkaProducer[String, String](props)
      //        partition.foreach(pair=>{
      //          producer.send(new ProducerRecord[String, String]("g7", "", pair._1))
      //        })
      //      })
      rdd.map(_.value()).flatMap(_.split(",")).map((_, 1)).reduceByKey(_ + _)
        .foreach(pair => {
          kafkaSink.value.send("g7", "", pair._1)
        })

      stream.asInstanceOf[CanCommitOffsets].commitAsync(offsetRanges)
    }


    ssc.start()
    ssc.awaitTermination()

  }

}
