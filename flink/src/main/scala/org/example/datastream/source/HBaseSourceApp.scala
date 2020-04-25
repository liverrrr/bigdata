package org.example.datastream.source

import org.apache.flink.addons.hbase.TableInputFormat
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._
import org.apache.hadoop.hbase.client.{Result, Scan}
import org.apache.hadoop.hbase.util.Bytes
import org.apache.flink.api.java.tuple.Tuple2


object HBaseSourceApp {

  val cf = Bytes.toBytes("access")
  val qualifier = Bytes.toBytes("url")
  val table_name = "ruozedata:access"

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    val hbaseDS = env.createInput(new TableInputFormat[Tuple2[String, String]] {
      private val reuse = new Tuple2[String, String]()

      override def getScanner: Scan = {
        val scan = new Scan()
        scan.addColumn(cf, qualifier)
      }

      override def getTableName: String = table_name

      override def mapResultToTuple(r: Result): Tuple2[String, String] = {
        val key = Bytes.toString(r.getRow)
        val value = Bytes.toString(r.getValue(cf, qualifier))
        reuse.setField(key, 0)
        reuse.setField(value, 1)
        reuse
      }

    })

    hbaseDS.print()

    env.execute(this.getClass.getSimpleName)
  }

}
