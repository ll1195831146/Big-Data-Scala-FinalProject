package org.csye7200.scala.spark.nbaRatingAndPredicting

import org.apache.spark.ml.feature.{StringIndexer, VectorAssembler}

object PipelineConstruction {

  val MVP_index: StringIndexer = new StringIndexer()
    .setInputCol("MVP")
    .setOutputCol("pre_MVP")


  // Create the feature columns
  val featureColumns: Array[String] = Array("bmi", "MVP", "pre_MVP")

  // Use the VectorAssembly to create the feature vector
  val vectorFeatureAssembler = new VectorAssembler()
    .setInputCols(featureColumns)
    .setOutputCol("features")


}