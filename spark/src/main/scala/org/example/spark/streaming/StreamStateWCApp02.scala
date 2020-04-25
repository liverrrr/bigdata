package org.example.spark.streaming

import org.apache.spark.streaming.{State, StateSpec, StreamingContext}
import org.example.spark.util.ContextUtils

/**
  * mapWithState它会按照时间线在每一个批次间隔返回之前的发生改变的或者新的key的状态，不发生变化的不返回
  */
object StreamStateWCApp02 {

  val checkpointDirectory = "./checkpoint_map/"


  def functionToCreateContext(): StreamingContext = {
    val ssc = ContextUtils.getStreamingContext(this.getClass.getSimpleName, 5) // new context
    val lines = ssc.socketTextStream("hadoop001", 9999) // create DStreams
    ssc.checkpoint(checkpointDirectory) // set checkpoint directory

    /**
      * value：当前值
      * state：当前状态
      */
    val mapfunc = (key: String, value: Option[Int], state: State[Int]) => {
      val newValue = value.getOrElse(0) + state.getOption().getOrElse(0)
      state.update(newValue)
      (key, newValue)
    }

    val result = lines.flatMap(_.split(","))
      .map((_, 1))
      .mapWithState(StateSpec.function(mapfunc))

    result.print()
    ssc
  }

  def main(args: Array[String]): Unit = {
    val context = StreamingContext.getOrCreate(checkpointDirectory, functionToCreateContext)

    context.start()
    context.awaitTermination()
  }

}
