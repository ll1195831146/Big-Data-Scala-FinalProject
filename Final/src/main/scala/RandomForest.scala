package scala

import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.ml.regression.{RandomForestRegressionModel, RandomForestRegressor}
import org.apache.spark.sql.SparkSession
import org.apache.spark.ml.feature.{StringIndexer, VectorAssembler}
import org.apache.spark.ml.feature.VectorIndexer

object RandomForest extends App with SparkConfig{

  val rootLogger = Logger.getRootLogger().setLevel(Level.WARN)

  val dataFrame = sc.read.format("CSV").option("header", "true").option("inferSchema", "true").load("data/00-17stats.csv")

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

  val labelDF = sc.sql("SELECT Voting FROM data").createOrReplaceTempView("label")

  var featureDF = sc.sql("SELECT * FROM data")

  println("featureDF:")
  featureDF.show()

  val colNames = featureDF.columns.drop(1).drop(1)

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

  // Split the data into training and test sets (30% held out for testing).
  val Array(trainingData, testData) = finalDF.randomSplit(Array(0.7, 0.3))


  // Train a RandomForest model.
  val numClasses = 2
  val categoricalFeaturesInfo = Map[Int, Int]()
  val numTrees = 3 // Use more in practice.
  val featureSubsetStrategy = "auto" // Let the algorithm choose.
  val maxDepth = 4
  val maxBins = 50
  val rf = new RandomForestRegressor()
    .setLabelCol("label")
    .setFeaturesCol("indexedFeatures")
    .setNumTrees(numTrees)
    .setFeatureSubsetStrategy(featureSubsetStrategy)
    .setMaxDepth(maxDepth)
    .setMaxBins(maxBins)

  //We define the Array with the stages of the pipeline
  val stages = Array(
    featureIndexer,
    rf
  )

  //Construct the pipeline
  val pipeline = new Pipeline().setStages(stages)

  // Train model. This also runs the indexer.
  val model = pipeline.fit(trainingData)

  // Make predictions.
  val predictions = model.transform(testData)

  //  // Select example rows to display.
  //  predictions.select("prediction", "label", "features").show(5)

  predictions.select("prediction", "label", "features").show(5)
  //println(predictions.count())

  //  //Save Model
  //  model.write.overwrite().save("target/tmp/Model")

  // Select (prediction, true label) and compute test error.
  val evaluator = new RegressionEvaluator()
    .setLabelCol("label")
    .setPredictionCol("prediction")
    .setMetricName("rmse")

  val rmse = evaluator.evaluate(predictions)

  println("Root Mean Squared Error (RMSE) on test data = " + rmse)


}

