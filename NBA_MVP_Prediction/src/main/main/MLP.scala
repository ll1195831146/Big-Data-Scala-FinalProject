package main

import org.apache.spark._
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import org.apache.spark.sql._
import org.apache.spark.sql.Dataset
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.classification.RandomForestClassifier
import org.apache.spark.ml.classification.RandomForestClassificationModel
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics
import org.apache.spark.ml.evaluation.BinaryClassificationEvaluator
import org.apache.spark.ml.feature.StringIndexer
import org.apache.spark.ml.tuning.ParamGridBuilder
import org.apache.spark.ml.tuning.CrossValidator
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.feature.Imputer
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.ml.classification.MultilayerPerceptronClassifier
import org.apache.spark.ml.regression.RandomForestRegressor
import org.apache.spark.ml.feature.PCA

object MLP extends App with MySparkConfig {
  val rootLogger = Logger.getRootLogger().setLevel(Level.WARN)
  /**
    * label：目标矩阵
    * features：特征矩阵
    * predict_label：预测矩阵
    * error：误差
    */

  val dataFrame = sc.read.format("CSV").option("header", "true").option("inferSchema", "true").load("data/00-17stats.csv")

  val data_cleaned = dataFrame.na.drop()
  //data_cleaned.show()
  data_cleaned.createOrReplaceTempView("data")

  val labelDF = sc.sql("SELECT Voting FROM data")
  labelDF.show()
  labelDF.createOrReplaceTempView("label")


  var featureDF = sc.sql("SELECT * FROM data")
  /**
    *修改tm, pos
    */
  featureDF = featureDF.drop("Pos", "Tm", "Player")

  featureDF.show()
  val colNames = featureDF.columns.dropRight(1)

  val assembler = new VectorAssembler()
    .setInputCols(colNames)
    .setOutputCol("features")

  val assembleDF = assembler.transform(featureDF).withColumnRenamed("Voting", "label")
  assembleDF.show()

  val finalDF = featureDF.withColumnRenamed("Voting", "label")
  finalDF.show()

  //  val pca = new PCA()
  //    .setInputCol("features")
  //    .setOutputCol("pcaFeatures")
  //    .setK(50)
  //    .fit(assembleDF)

  //  val resultDF = pca.transform(assembleDF).select("pcaFeatures", "label").withColumnRenamed("pcaFeatures", "features")

  val splitSeed = 12345L
  val Array(trainingData, testData) = assembleDF.randomSplit(Array(0.80, 0.20), splitSeed)

  val layers = Array[Int](50, 16, 32, 100)
  //create the trainer and set its parameters
  val mlp = new MultilayerPerceptronClassifier()
    .setLayers(layers) // Sets the value of param [[layers]].                .
    .setTol(1E-4) //Set the convergence tolerance of iterations. Smaller value will lead to higher accuracy with the cost of more iterations. Default is 1E-4.
    .setBlockSize(128) // Sets the value of param [[blockSize]], where, the default is 128.
    .setSeed(12345L) // Set the seed for weights initialization if weights are not set
    .setMaxIter(100); // Set the maximum number of iterations. Default is 100.
  // ************************************************************
  println("Training model with MLP")
  // ************************************************************


  val cvModel = mlp.fit(trainingData)

  //val model = classifier.fit(trainingData)
  val predictions = cvModel.transform(testData)
  predictions.show()

  val evaluator = new MulticlassClassificationEvaluator().setLabelCol("label").setPredictionCol("prediction")


  val evaluator1 = evaluator.setMetricName("accuracy")
  val evaluator2 = evaluator.setMetricName("weightedPrecision")
  val evaluator3 = evaluator.setMetricName("weightedRecall")
  val evaluator4 = evaluator.setMetricName("f1")

  // compute the classification accuracy, precision, recall, f1 measure and error on test data.
  val accuracy = evaluator1.evaluate(predictions)
  val precision = evaluator2.evaluate(predictions)
  val recall = evaluator3.evaluate(predictions)
  val f1 = evaluator4.evaluate(predictions)

  // Print the performance metrics
  println("Accuracy = " + accuracy)
  println("Precision = " + precision)
  println("Recall = " + recall)
  println("F1 = " + f1)
  println(s"Test Error = ${1 - accuracy}")

}