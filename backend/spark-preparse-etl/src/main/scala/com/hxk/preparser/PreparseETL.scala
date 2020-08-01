package com.hxk.preparser

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Encoders, SaveMode, SparkSession}

object PreparseETL {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("PreparseETL")
      .enableHiveSupport()
      .master("local") //保证集群HDFS和Hive的metastore服务启动
      .getOrCreate()

   val rawdataInputPath = spark.conf.get("spark.traffic.analysis.rawdata.input",
     "hdfs://master:9999/user/hadoop-twq/traffic-analysis/rawlog/20180616")

    val numberPartitions = spark.conf.get("spark.traffic.analysis.rawdata.numberPartitions","2").toInt

    //从HDFS中文件创建RDD
    val preParsedLogRDD: RDD[PreparsedLog] = spark.sparkContext.textFile(rawdataInputPath)
      .flatMap(line => Option(WebLogPreParser.parse(line)))

    //RDD -> DS
    val preParsedLogDS = spark.createDataset(preParsedLogRDD)(Encoders.bean(classOf[PreparsedLog]))

    //小文件合并
    preParsedLogDS.coalesce(numberPartitions)
      .write
      .mode(SaveMode.Append)
      .partitionBy("year","month","day")
      .saveAsTable("rawdata.web")  //Hive的rawdata数据库

    //hadoop fs -chmod -R 777 /user/hive/warehouse/rawdata.db

    spark.stop()

  }
}
