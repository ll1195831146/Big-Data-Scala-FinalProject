package main

import org.apache.spark.sql.SparkSession

object Stats extends App with MySparkConfig {

  println("import data set: ")
  val dataFrame = sc.read.format("CSV").option("header", "true").option("inferSchema", "true").load("data/00-17stats.csv")

  dataFrame.show()
  dataFrame.printSchema()


  val data_cleaned = dataFrame.na.drop()

  println("dataframe size: " + dataFrame.count())
  println("datacleaned size:  " +data_cleaned.count())

}


