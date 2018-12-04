package org.csye7200.scala.spark.nbaRatingAndPredicting

import java.io.File

import org.apache.spark.sql.SparkSession

object CreateSparkSession {

  def createSession(appName: String = "mvp-pipeline"): SparkSession = {

    val spark = SparkSession.builder()
      .master("local[*]")
      .appName(appName)
      .getOrCreate()

    spark
  }
}
