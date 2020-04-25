package org.example.spark.streaming

import org.apache.kafka.common.TopicPartition
import org.apache.spark.streaming.kafka010.OffsetRange
import org.example.spark.util.RedisUtil

object RedisOffsetManager extends OffsetManager {
  /**
    * 从redis获取offset
    * @param key topic+"_"+groupId
    */
  override def obtainOffsets(key: String): Map[TopicPartition, Long] = {
    val jedis = RedisUtil.getJedis
    val offsets = jedis.hgetAll(key)
    val topic = key.split("_")(0)
    var fromOffsets = Map[TopicPartition, Long]()
    import scala.collection.JavaConversions._
    offsets.foreach(x=>{
      fromOffsets += new TopicPartition(topic,x._1.toInt) -> x._2.toLong
    })
    RedisUtil.closeJedis(jedis)
    fromOffsets
  }

  /**
    * 依照key来存储offset到redis
    *
    * @param offsetRanges
    * @param key topic+"_"+groupId
    */
  override def storeOffsets(offsetRanges: Array[OffsetRange],key:String): Unit = {

    val jedis = RedisUtil.getJedis
    offsetRanges.foreach { o =>
      jedis.hset(key,o.partition+"",o.untilOffset+"")
      println(s"${o.topic} ${o.partition} ${o.fromOffset} ${o.untilOffset}")
    }
    RedisUtil.closeJedis(jedis)

  }
}
