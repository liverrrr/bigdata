package org.example.spark.streaming

import org.example.spark.util.{ContextUtils, RedisUtil}

object StreamOutputApp {

  def main(args: Array[String]): Unit = {
    val ssc = ContextUtils.getStreamingContext(this.getClass.getSimpleName, 5)

    val lines = ssc.socketTextStream("hadoop001",9999)
    val result = lines.flatMap(_.split(",")).map((_,1)).reduceByKey(_+_)

    /**
      * 这样会报错connection无法序列化，不用
      * dstream.foreachRDD { rdd =>
      * val connection = createNewConnection()  // executed at the driver
      *   rdd.foreach { record =>
      *     connection.send(record) // executed at the worker
      * }
      * }
      */

    /**
      * WC这种统计维度来说
      * Redis的使用关键点：如何选择合适的数据类型 hash/set/list
      */
    result.foreachRDD(rdd=>{
      rdd.foreachPartition(partition=>{
        val jedis = RedisUtil.getJedis
        partition.foreach(pair=>{
          jedis.hincrBy("ruozedata_redis_wc",pair._1,pair._2)
        })
        RedisUtil.closeJedis(jedis)
      })
    })

    ssc.start()
    ssc.awaitTermination()
  }

}
