package org.csye7200.scala.spark.nbaRatingAndPredicting

//import org.apache.spark.ml.Pipeline
//import org.apache.spark.ml.classification.LogisticRegression
//import org.apache.spark.ml.evaluation.BinaryClassificationEvaluator
//import org.apache.spark.sql.SparkSession

object RFModel {

//    val spark: SparkSession = SparkSessionCreate.createSession()
//
//    import spark.implicits._
//
//    val numFolds = 10
//    val MaxIter: Seq[Int] = Seq(100)
//    val RegParam: Seq[Double] = Seq(1.0) // L2 regularization param, set 0.10 with L1 regularization
//    val Tol: Seq[Double] = Seq(1e-8)
//    val ElasticNetParam: Seq[Double] = Seq(1.0) // Combination of L1 and L2
//
//    val predictions = crossValidModel.transform(DataProcessing.testingSet)
//    predictions.select("predictions", "label")
//      .show(10)
//    //    val result = predictions.select("label", "prediction", "probability")
//    //    val resutDF = result.withColumnRenamed("prediction", "Predicted_label")
//    //    resutDF.show(10)
}