package org.csye7200.scala.spark.nbaRatingAndPredicting

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.functions._
//import spark.implicits._
import scala.io.Source


object Stats extends App {

  print(s"import data set")
  val train_data = Source.fromFile("data/00-17stats.csv")
  val test_data = Source.fromFile("data/17-18stats.csv")

  val filterDF = train_data.filter($"Player".isNotNull && length(trim($"Player")) > 0)

  for (line <- filterDF.getLines) {
    print(line)
    val cols = line.split(",").map(_.trim)
    // do whatever you want with the columns here
    //    println(cols)
    println(s"${cols(0)}|${cols(1)}|${cols(2)}|${cols(3)}")
  }
  train_data.close()
  test_data.close()

}