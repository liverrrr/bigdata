package org.example.spark.structured

import org.apache.spark.sql.SparkSession

object QuickExampleApp {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("QuickExampleApp")
      .master("local[2]")
      .getOrCreate()

    import spark.implicits._

    val lines = spark.readStream
      .format("socket")
      .option("host", "hadoop001")
      .option("port", "9999")
      .load()

    val words = lines.as[String].flatMap(_.split(","))
    val wordCounts = words.groupBy("value").count()

    val query = wordCounts.writeStream
      .outputMode("complete")
      .format("console")
      .start()

    query.awaitTermination()
  }

}
