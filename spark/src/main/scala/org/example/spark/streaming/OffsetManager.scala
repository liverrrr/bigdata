package org.example.spark.streaming

import org.apache.kafka.common.TopicPartition
import org.apache.spark.streaming.kafka010.OffsetRange

trait OffsetManager {

  /**
    * 从外部获取offset
    * @param key
    */
  def obtainOffsets(key:String):Map[TopicPartition, Long]

  /**
    * 依照key来存储offset到外部
    * @param offsetRanges
    */
  def storeOffsets(offsetRanges:Array[OffsetRange],key:String)

}
