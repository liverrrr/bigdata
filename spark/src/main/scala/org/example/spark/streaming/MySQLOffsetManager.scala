package org.example.spark.streaming

import org.apache.kafka.common.TopicPartition
import org.apache.spark.streaming.kafka010.OffsetRange
import scalikejdbc.config.DBs
import scalikejdbc.{DB, SQL}

object MySQLOffsetManager extends OffsetManager {
  /**
    * 从mysql获取offset
    *
    * @param key topic+"_"+groupId
    */
  override def obtainOffsets(key: String): Map[TopicPartition, Long] = {
    DBs.setupAll()
    var fromOffsets = Map[TopicPartition, Long]()
    val splits = key.split("_")
    val topic = splits(0)

    DB.readOnly(implicit session => {
      SQL("select * from test_offset where topic_groupId=?")
        .bind(key)
        .map(rs => {
          fromOffsets += new TopicPartition(topic,rs.int("kafka_partition")) -> rs.int("kafka_offset").toLong
      }).list().apply()
    })

    DBs.close()
    fromOffsets
  }

  /**
    * 依照key来存储offset到mysql
    *
    * @param offsetRanges
    * @param key topic+"_"+groupId
    */
  override def storeOffsets(offsetRanges: Array[OffsetRange], key: String): Unit = {
    DBs.setupAll()

    offsetRanges.foreach { o =>
      DB.autoCommit(implicit session => {
        SQL("insert into test_offset(topic_groupId,kafka_partition,kafka_offset) values(?,?,?) ON DUPLICATE KEY UPDATE kafka_offset=?")
          .bind(key, o.partition.toInt, o.untilOffset.toInt,o.untilOffset.toInt)
          .update()
          .apply()
      })
      println(s"${o.topic} ${o.partition} ${o.fromOffset} ${o.untilOffset}")
    }

    DBs.close()
  }
}
