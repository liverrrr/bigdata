package org.example.spark.sql

import java.io.File

import org.apache.hadoop.fs.FileUtil
import org.apache.hadoop.io.compress.GzipCodec
import org.apache.spark.sql.{SaveMode, SparkSession}


object ExtDSApp {

  def text(spark: SparkSession): Unit = {
    import spark.implicits._
    //    val df = spark.read.format("text").load("data/input/access.log")
    val ds = spark.read.textFile("data/input/access.log")

    FileUtil.fullyDelete(new File("data/out"))

    ds.map(x => {
      val splits = x.split("\t")
      (splits(0), splits(1))
    }).rdd.saveAsTextFile("data/out", classOf[GzipCodec])
  }

  def json(spark: SparkSession) = {
    import spark.implicits._
    val df = spark.read.format("json").load("data/input/people.json")
    df.filter($"name" =!= "Andy").show()
  }

  def csv(spark: SparkSession) = {
    import spark.implicits._
    val df = spark.read.format("csv")
      .option("header", "true")
      .option("sep", ";")
      .load("data/input/people.csv")

    df.select("name", "job").filter('name === "Bob").write.format("csv")
      .option("header", "true")
      .option("sep", ";")
      .mode(SaveMode.Overwrite).save("data/out")
  }

  def jdbc(spark: SparkSession, input: String) = {
    import spark.implicits._
    val df = spark.read.format("csv")
      .option("header", "true")
      .option("sep", ";")
      .load(input)

    df.write
      .format("jdbc")
      .option("url", "jdbc:mysql://hadoop001:3306")
      .option("dbtable", "g7.sparkSQL_test")
      .option("user", "ruoze")
      .option("password", "ruozedata")
      .mode(SaveMode.Overwrite)
      .save()
  }

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      //      .master("local[2]")
      //      .appName("ExtDSApp")
      .getOrCreate()

    //    text(spark)
    //    json(spark)
    //    csv(spark)
    jdbc(spark, args(0))


    spark.close()

  }


}
