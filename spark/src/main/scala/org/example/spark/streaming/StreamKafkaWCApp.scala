package org.example.spark.streaming

import org.example.spark.util.ContextUtils
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe

object StreamKafkaWCApp {

  def main(args: Array[String]): Unit = {
//        val ssc = ContextUtils.getStreamingContext(this.getClass.getSimpleName, 5)
    val conf = new SparkConf()
    val ssc = new StreamingContext(conf, Seconds(5))

    if (args.length != 3){
      println("Case StreamKafkaWCApp <brokers> <topic> <gooupId>")
      System.exit(-1)
    }

    val brokers = args(0)
    val topic = args(1)
    val gooupId = args(2)


    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> brokers, //"hadoop001:9092,hadoop001:9093,hadoop001:9094",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> gooupId, //"use_a_separate_group_id_for_each_stream",
      "auto.offset.reset" -> "earliest",
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )


    val topics = Array(topic)
    val stream = KafkaUtils.createDirectStream[String, String](
      ssc,
      PreferConsistent,
      Subscribe[String, String](topics, kafkaParams)
    )

    stream.map(_.value()).flatMap(_.split(",")).map((_, 1)).reduceByKey(_ + _).print()

    ssc.start()
    ssc.awaitTermination()
  }

}
