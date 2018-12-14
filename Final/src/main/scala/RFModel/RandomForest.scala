package RFModel

import SparkConfig.SparkConfig
import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.{Pipeline, PipelineModel}
import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.ml.feature.{VectorAssembler, VectorIndexer}
import org.apache.spark.ml.regression.{RandomForestRegressionModel,RandomForestRegressor}
import org.apache.spark.ml
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._

object RandomForest extends App with SparkConfig{



//  val dataFrame = sc.read.format("CSV").option("header", "true").option("inferSchema", "true").load("data/00-17stats.csv")
//
//  val data_cleaned = dataFrame.na.drop()
//  //data_cleaned.show()

    def FeatureDataFrame(data: DataFrame): DataFrame= {
      val rootLogger = Logger.getRootLogger().setLevel(Level.WARN)
      data.createOrReplaceTempView("data")

      val labelDF = sc.sql("SELECT Voting FROM data").createOrReplaceTempView("label")


      var featureDF = sc.sql("SELECT * FROM data")

      println("featureDF:")
      featureDF.show()

      val colNames = featureDF.columns.drop(1).drop(1).drop(2)

      val assembler = new VectorAssembler()
        .setInputCols(colNames)
        .setOutputCol("features")

      val assembleDF = assembler.transform(featureDF)
      println("assembleDF:")
      assembleDF.show()
      //
      val finalDF = assembleDF.withColumnRenamed("Voting", "label")
      println("finalDF:")
      finalDF
    }

      def RFModel(data: DataFrame)={
      val featureIndexer = new VectorIndexer()
        .setInputCol("features")
        .setOutputCol("indexedFeatures")
      // Config a RandomForest model.
      val numTrees = 3 // Use more in practice.
      val featureSubsetStrategy = "auto" // Let the algorithm choose.
      val maxDepth = 4
      val maxBins = 50

      //Train a RandomForest model.


      val rf = new RandomForestRegressor()
        .setLabelCol("label")
        .setFeaturesCol("features")
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
      val model = pipeline.fit(data)

      model
    }
      def prediction(model: PipelineModel, data: DataFrame): Unit ={
        val predictions = model.transform(data)
        predictions.select("Player", "prediction").orderBy(desc("prediction")) show (10)

      }

      def Metrix(model: PipelineModel, data: DataFrame): Unit = {

        // Split the data into training and test sets (30% held out for testing).
        val Array(trainingData, testData) = data.randomSplit(Array(0.7, 0.3))

        // Make predictions.
        val predictions = model.transform(testData)

          // Select example rows to display.
        predictions.select("prediction", "label", "features").show(5)

        val evaluator = new RegressionEvaluator()
          .setLabelCol("label")
          .setPredictionCol("prediction")
          .setMetricName("rmse")
        //
        val rmse = evaluator.evaluate(predictions)

        println("Root Mean Squared Error (RMSE) on test data = " + rmse)
      }

}
