package org.example.spark.util

import redis.clients.jedis.{Jedis, JedisPool, JedisPoolConfig}

object RedisUtil {

  def getJedis: Jedis ={
    val poolConfig = new JedisPoolConfig
    poolConfig.setTestOnBorrow(true)
    poolConfig.setMaxIdle(20)
    poolConfig.setMaxWaitMillis(100000)
    new JedisPool(poolConfig,"hadoop001",6375).getResource
  }


  def closeJedis(jedis: Jedis): Unit ={
    if (jedis != null){
      jedis.close()
    }
  }

  def main(args: Array[String]): Unit = {
    val jedis = getJedis
    val value = jedis.get("name")
    println(value)
    jedis.close()
  }

}
