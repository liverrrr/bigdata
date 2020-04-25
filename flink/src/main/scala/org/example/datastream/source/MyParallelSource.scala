package org.example.datastream.source

import org.apache.flink.streaming.api.functions.source.{ParallelSourceFunction, SourceFunction}
import org.example.domain.Domain.Access

import scala.util.Random

class MyParallelSource extends ParallelSourceFunction[Access] {
  private var isRunning = true

  override def run(ctx: SourceFunction.SourceContext[Access]): Unit = {
    val domains = List("ruozedata.com", "dongqiudi.com", "zhibo8.com")
    val random = new Random()
    while (isRunning) {
      val time = System.currentTimeMillis() + ""
      val domain = domains(random.nextInt(domains.length))
      val flow = random.nextInt(10000)
      1.to(10).map(x => ctx.collect(Access(time, domain, flow)))
    }
  }

  override def cancel(): Unit = {
    isRunning = false
  }
}
