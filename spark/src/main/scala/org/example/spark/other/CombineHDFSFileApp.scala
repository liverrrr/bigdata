package org.example.spark.other

import java.net.URI

import org.apache.hadoop.conf.Configuration
import org.apache.commons.lang3.math.NumberUtils
import org.apache.hadoop.fs.{FileSystem, InvalidPathException, Path}
import org.apache.spark.internal.Logging
import org.apache.spark.{SparkConf, SparkContext}

import scala.math.ceil

object CombineHDFSFileApp extends Logging{

  def main(args: Array[String]): Unit = {
    System.setProperty("HADOOP_USER_NAME", "hdfs")
    val conf = new SparkConf().setAppName(this.getClass.getSimpleName)
      .setMaster("local[2]")
    val sc = new SparkContext(conf)

    val hdfsUri = conf.getOption("spark.hdfs.uri").getOrElse("hdfs://hadoop004:8020")
    var mergePath = conf.getOption("spark.merge.path").getOrElse("/flume/rd/logs/")
    val mergePathDate = conf.getOption("spark.merge.path.date").getOrElse("2020-05-14")
    val targetFileSize = conf.getOption("spark.target.file.size").getOrElse("100").toInt

    val fileSystem = FileSystem.get(new URI(hdfsUri), new Configuration(), "hdfs")
    mergePath = mergePath + mergePathDate
    val fileStatus = fileSystem.getFileStatus(new Path(mergePath))
    if (!fileStatus.isDirectory) {
      throw new InvalidPathException("目标路径不是一个文件夹，请检查")
    }
    val files = fileSystem.listFiles(new Path(mergePath), true)
    var filesNum = 0
    var sumFileSize = 0L
    while (files.hasNext) {
      val file = files.next()
      sumFileSize = sumFileSize + file.getLen
      filesNum = filesNum + 1
    }

    val numPartition = ceil(sumFileSize / (1024 * 1024 * 1.0) / targetFileSize).toInt
    logError("合并之前文件个数:" + filesNum + "\t" +"合并之后文件个数:" + numPartition)
    val srcPath = hdfsUri + mergePath
    val targetPath = hdfsUri + "/output/" + mergePathDate
    if (fileSystem.exists(new Path(targetPath))) {
      fileSystem.delete(new Path(targetPath), true)
    }
    sc.textFile(srcPath)
      .coalesce(numPartition)
      .saveAsTextFile(targetPath)

    //检查大小是否一致
    val newSumFileSize = fileSystem.getContentSummary(new Path(targetPath)).getLength
    logError("合并后文件总大小:" + newSumFileSize + "\t" + "合并之前文件总大小:" + sumFileSize)
    if (NumberUtils.compare(newSumFileSize, sumFileSize) == 0) {
      fileSystem.delete(new Path(targetPath + "/_SUCCESS"))
      fileSystem.delete(new Path(srcPath),true)
      fileSystem.rename(new Path(targetPath), new Path(srcPath).getParent)
    }
  }

}
