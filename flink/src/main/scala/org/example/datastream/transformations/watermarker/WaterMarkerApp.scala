package org.example.datastream.transformations.watermarker

import java.util.Properties

import org.apache.commons.lang3.time.DateFormatUtils
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala.function.WindowFunction
import org.apache.flink.streaming.api.watermark.Watermark
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.streaming.util.serialization.SimpleStringSchema
import org.apache.flink.util.Collector

import scala.math._

object WaterMarkerApp {

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    env.setParallelism(1)

    val properties = new Properties()
    properties.setProperty("bootstrap.servers", "localhost:9092")
    properties.setProperty("group.id", "ruoze")
    properties.setProperty("request.timeout.ms", "90000")
    val myConsumer = new FlinkKafkaConsumer[String]("ruozedata", new SimpleStringSchema(), properties)

    env.addSource(myConsumer)
      .map(str => {
        val splits = str.split(",")
        val uid = splits(0)
        val timestamp = splits(1).toLong
        (uid, timestamp)
      }).assignTimestampsAndWatermarks(new AssignerWithPeriodicWatermarks[(String, Long)] {
      var currentMaxTimestamp: Long = _
      var currentWatermark: Watermark = _
      val maxOutOrderTime = 5000L //5s
      val pattern = "yyyy-MM-dd HH:mm:ss.SSS"

      override def getCurrentWatermark: Watermark = {
        currentWatermark = new Watermark(currentMaxTimestamp - maxOutOrderTime)
        currentWatermark
      }

      override def extractTimestamp(element: (String, Long), previousElementTimestamp: Long): Long = {
        val timestamp = element._2
        currentMaxTimestamp = max(timestamp, currentMaxTimestamp)
        if (currentWatermark != null) {
          println("key: " + element._1 + " timestamp = " + timestamp + "|" + DateFormatUtils.format(timestamp, pattern) + " "
            + "currentMaxTimestamp = " + currentMaxTimestamp + "|" + DateFormatUtils.format(currentMaxTimestamp, pattern) + " "
            + "currentWatermark = " + currentWatermark.getTimestamp + "|" + DateFormatUtils.format(currentWatermark.getTimestamp, pattern))
        }
        timestamp
      }
    })
      .keyBy(_._1)
      .timeWindow(Time.seconds(3))
      .apply(new WindowsFunctionTest)
      .print()

    env.execute(this.getClass.getSimpleName)
  }

  class WindowsFunctionTest extends WindowFunction[(String, Long), (String, Int, String, String, String, String), String, TimeWindow] {
    override def apply(key: String, window: TimeWindow,
                       input: Iterable[(String, Long)],
                       out: Collector[(String, Int, String, String, String, String)]): Unit = {
      val list = input.toList.sortBy(_._2)
      val pattern = "yyyy-MM-dd HH:mm:ss.SSS"
      out.collect((key, input.size, DateFormatUtils.format(list.head._2, pattern), DateFormatUtils.format(list.last._2, pattern)
        , DateFormatUtils.format(window.getStart, pattern), DateFormatUtils.format(window.getEnd, pattern)))
    }
  }

}
