package org.example.spark.streaming

import org.apache.spark.streaming.StreamingContext
import org.example.spark.util.ContextUtils

object StreamStateWCApp01 {

  val checkpointDirectory="./checkpoint_update/"

  def functionToCreateContext(): StreamingContext = {
    val ssc = ContextUtils.getStreamingContext(this.getClass.getSimpleName, 5)   // new context
    val lines = ssc.socketTextStream("hadoop001", 9999) // create DStreams
    ssc.checkpoint(checkpointDirectory)   // set checkpoint directory
    val result = lines.flatMap(_.split(",")).map((_, 1)).updateStateByKey(updateFunction)
    result.print()
    ssc
  }


  def updateFunction(newValues: Seq[Int], preValues: Option[Int]): Option[Int] = {
    val curr = newValues.sum
    val pre = preValues.getOrElse(0)
    Some(curr + pre)
  }


  def main(args: Array[String]): Unit = {
    val context = StreamingContext.getOrCreate(checkpointDirectory, functionToCreateContext)

    context.start()
    context.awaitTermination()
  }

}
