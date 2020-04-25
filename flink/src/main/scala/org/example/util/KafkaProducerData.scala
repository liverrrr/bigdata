package org.example.util

import java.util.Properties

import org.apache.commons.lang3.time.DateFormatUtils
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.slf4j.LoggerFactory

import scala.util.Random

object KafkaProducerData {

  private val logger = LoggerFactory.getLogger(KafkaProducerData.getClass)

  def main(args: Array[String]): Unit = {
    new MyThread(1000,true).start()
  }

  private def pushDataToKafka(props: Properties,timeOut:Long,enableOutOrder:Boolean = false) = {
    val producer = new KafkaProducer[String, String](props)
    while (true){
      var word:String = null
      if (enableOutOrder){
        for (i <- 1 to 50){
          val random = Random.nextInt(10)
          word = "a," + (System.currentTimeMillis() - random * 1000)
        }
      } else {
        word = "a," + System.currentTimeMillis()
      }
      logger.error("word:" + word + "|" +DateFormatUtils.format(word.split(",")(1).toLong,"yyyy-MM-dd HH:mm:ss"))
      producer.send(new ProducerRecord[String, String]("ruozedata", "", word))
      Thread.sleep(timeOut)
    }
    producer.close()
  }

  class MyThread(timeOut:Long,enableOutOrder:Boolean) extends Thread{
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092")
    props.put("acks", "all")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    override def run(): Unit = pushDataToKafka(props,timeOut,enableOutOrder)
  }
}

