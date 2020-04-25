package org.example.spark.streaming

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

class KafkaSink(createProducer: () => KafkaProducer[String, String]) extends Serializable {

  private lazy val producer = createProducer()

  def send(topic: String, value: String): Unit = producer.send(new ProducerRecord(topic, value))

  def send(topic: String, key: String, value: String): Unit = producer.send(new ProducerRecord(topic, key, value))
}

object KafkaSink {
  def apply(config: Properties): KafkaSink = {
    val f = () => {
      new KafkaProducer[String, String](config)
    }
    new KafkaSink(f)
  }
}
