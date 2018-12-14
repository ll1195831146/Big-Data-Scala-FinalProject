package DataCleaning

import SparkConfig.SparkConfig
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.DataFrame
import org.apache.spark.ml.feature.{StringIndexer, VectorAssembler, VectorIndexer}


object DataCleaning extends App with SparkConfig {

  def loadDataset(filePath: String): DataFrame = {

    val dataFrame = sc.read.format("CSV").option("header", "true").option("inferSchema", "true").load(filePath)

    val data_cleaned = dataFrame.na.drop()
    data_cleaned.printSchema()
    data_cleaned.createOrReplaceTempView("data")
    sc.sql("select * from data")

  }

  def dataIndexed(data:DataFrame)={

    //StringIndexer convert Pos, Tm
    val posIndexer = new StringIndexer()
      .setInputCol("Pos")
      .setOutputCol("posIndex")

    val posIndexerDF = posIndexer.fit(data).transform(data)

    val tmIndexer = new StringIndexer()
      .setInputCol("Tm")
      .setOutputCol("tmIndex")

    val tmIndexerDF = tmIndexer.fit(posIndexerDF).transform(posIndexerDF)

    tmIndexerDF.show()

    val data_indexed = tmIndexerDF.drop("Pos", "Tm")

    print("data_indexed")

    data_indexed.show()
    data_indexed
  }
}
