package org.example.spark.warehouse

import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.{HasOffsetRanges, KafkaUtils}
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.example.spark.util.ContextUtils

object MaxwellKafkaStreamApp {

  def main(args: Array[String]): Unit = {

    val ssc = ContextUtils.getStreamingContext(this.getClass.getSimpleName, 5)

    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> "hadoop001:9092,hadoop001:9093,hadoop001:9094",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "use_a_separate_group_id_for_each_stream",
      "auto.offset.reset" -> "earliest",
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )



    val topics = Array("maxwell")
    val stream = KafkaUtils.createDirectStream[String, String](
      ssc,
      PreferConsistent,
      Subscribe[String, String](topics, kafkaParams)
    )

    stream.map(_.value()).print()


    ssc.start()
    ssc.awaitTermination()

  }

}
