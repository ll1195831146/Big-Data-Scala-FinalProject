package scala

import jdk.incubator.http.internal.frame.DataFrame
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql._

object DataCleaning extends App with SparkConfig {

  println("import dataset: ")
  val dataFrame = sc.read.format("CSV").option("header", "true").option("inferSchema", "true").load("String")

  //    dataFrame.show()
  //    dataFrame.printSchema()


  val data_cleaned = dataFrame.na.drop()

  //    println("dataframe size: " + dataFrame.count())
  //    println("datacleaned size:  " +data_cleaned.count())
  //
}
