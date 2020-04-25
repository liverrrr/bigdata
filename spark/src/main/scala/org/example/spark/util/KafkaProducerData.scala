package org.example.spark.util

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, Producer, ProducerRecord}
import org.slf4j.{Logger, LoggerFactory}

import scala.util.Random

object KafkaProducerData {

  private val logger = LoggerFactory.getLogger(KafkaProducerData.getClass)

  def main(args: Array[String]): Unit = {

    val props = new Properties()
    props.put("bootstrap.servers", "hadoop001:9092,hadoop001:9093,hadoop001:9094")
    props.put("acks", "all")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

    val producer = new KafkaProducer[String, String](props)
    for (i <- 0 to 10) {
      val word = String.valueOf((new Random().nextInt(6) + 'a').toChar)
      logger.error("word:"+word)
      producer.send(new ProducerRecord[String, String]("sparkstream", "", word))
    }
    producer.close()
  }

}
