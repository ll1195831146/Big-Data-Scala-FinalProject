package main

import org.apache.spark.sql.SparkSession

trait MySparkConfig {

  val sc = SparkSession
    .builder()
    .appName("app")
    .master("local")
    .getOrCreate()
}
