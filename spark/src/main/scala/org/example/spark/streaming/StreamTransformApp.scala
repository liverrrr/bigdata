package org.example.spark.streaming

import org.example.spark.util.ContextUtils


/**
  * transform DStream与RDD互操作
  * 流处理的时候，有一个数据来源于文本或者是其他的  RDD
  * 另外一个数据是来自Kafka、或者其他的数据源  DStream
  */
object StreamTransformApp {

  def main(args: Array[String]): Unit = {
    val ssc = ContextUtils.getStreamingContext(this.getClass.getSimpleName, 5)

    val blackRdd = ssc.sparkContext.parallelize(List(("dengdi",1)))

    val lines = ssc.socketTextStream("hadoop001",9999)
    lines.map(x=>(x.split(",")(0),x))
      .transform(rdd=>{
      rdd.leftOuterJoin(blackRdd).filter(_._2._2.getOrElse(0) != 1).map(_._2._1)
    }).print()

    ssc.start()
    ssc.awaitTermination()
  }

}
