package SparkConfig

import org.apache.spark.sql.SparkSession

trait SparkConfig {

  val sc = SparkSession
    .builder()
    .appName("app")
    .master("local")
    .getOrCreate()

}
