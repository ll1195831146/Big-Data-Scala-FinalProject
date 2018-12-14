
import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.ml.regression.{RandomForestRegressionModel, RandomForestRegressor}
import org.apache.spark.sql.SparkSession
import org.apache.spark.ml.feature.{StringIndexer, VectorAssembler}
import org.apache.spark.ml.feature.VectorIndexer

object PredictingMVP extends App with SparkConfig {
  val rootLogger = Logger.getRootLogger().setLevel(Level.WARN)

  val dataFrame = sc.read.format("CSV").option("header", "true").option("inferSchema", "true").load("data/17-18stats.csv")

  val data_cleaned = dataFrame.na.drop()
  //data_cleaned.show()

  val posIndexer = new StringIndexer()
    .setInputCol("Pos")
    .setOutputCol("posIndex")

  val posIndexerDF = posIndexer.fit(data_cleaned).transform(data_cleaned)

  val tmIndexer = new StringIndexer()
    .setInputCol("Tm")
    .setOutputCol("tmIndex")

  val tmIndexerDF = tmIndexer.fit(posIndexerDF).transform(posIndexerDF)

  tmIndexerDF.show()

  val data_indexed = tmIndexerDF.drop("Pos", "Tm", "Player")

  print("data_indexed")

  data_indexed.show()

  data_indexed.createOrReplaceTempView("data")

  var featureDF = sc.sql("SELECT * FROM data")

  println("featureDF:")
  featureDF.show()

  val colNames = featureDF.columns.drop(1)


  val assembler = new VectorAssembler()
    .setInputCols(colNames)
    .setOutputCol("features")

  val assembleDF = assembler.transform(featureDF)
  println("assembleDF:")
  assembleDF.show()

  val finalDF = assembleDF.withColumnRenamed("Voting", "label")
  println("finalDF:")
  finalDF.show()

  val featureIndexer = new VectorIndexer()
    .setInputCol("features")
    .setOutputCol("indexedFeatures")

  val model = RandomForestRegressionModel.load("tmp/rf_model")

  val predictions = model.transform(assembleDF.select("features"))
  predictions.select("prediction", "label", "features").show(5)

}
