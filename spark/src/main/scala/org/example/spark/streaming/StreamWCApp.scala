package org.example.spark.streaming

import org.example.spark.util.ContextUtils


object StreamWCApp {

  def main(args: Array[String]): Unit = {

    /**
      * 默认是local[2]
      * streaming中local数必须大于receiver的数，否则不能处理数据
      */
    val ssc = ContextUtils.getStreamingContext(this.getClass.getSimpleName, 5)

    val lines = ssc.socketTextStream("hadoop001",9999)
    val result = lines.flatMap(_.split(",")).map((_,1)).reduceByKey(_+_)

    result.print()

    ssc.start()
    ssc.awaitTermination()

  }

}
