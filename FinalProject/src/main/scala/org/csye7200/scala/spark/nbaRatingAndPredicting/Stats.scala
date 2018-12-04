package org.csye7200.scala.spark.nbaRatingAndPredicting


import scala.io.Source
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.Row
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.Column
import org.apache.spark.sql.DataFrameReader
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder
import org.apache.spark.sql.Encoder
import org.apache.spark.sql.functions._
import org.apache.spark.sql.DataFrameStatFunctions


object Stats extends App {

  val spark: SparkSession = CreateSparkSession.createSession()

  import spark.implicits._

//  val sc: SparkContext // 已有的 SparkContext.
//  val sqlContext = new org.apache.spark.sql.SQLContext(sc)
//
//  val df = sqlContext.read.json("data/00-17stats.csv")
//
//  // 将DataFrame内容打印到stdout
//  df.show()


  println(s"import data set: ")
  val dataFrame = spark.read.format("CSV").option("header","true").load("data/00-17stats.csv")
//  val test = spark.read.csv("data/00-17stats.csv")
  val train_data=dataFrame.na.drop()
//  val train_data = Source.fromFile("data/00-17stats.csv")
//  val test_data = Source.fromFile("data/17-18stats.csv")
  train_data.show()
//  val data1 = train_data.toDF
//  data1.limit(10).show
//  val filterDF = train_data.filter($"Player".isNotNull && length(trim($"Player")) > 0)
//  train_data.where(train_data.col("Player").isNotNull)
//  for (line <- train_data.getLines) {
////    println(line)
//    val cols = line.split(",").map(_.trim)
    // do whatever you want with the columns here
//    println(s"${cols(0)}|${cols(1)}|${cols(2)}|${cols(3)}")
//    println(cols)
//  }
//  train_data.close()
//  test_data.close()

}