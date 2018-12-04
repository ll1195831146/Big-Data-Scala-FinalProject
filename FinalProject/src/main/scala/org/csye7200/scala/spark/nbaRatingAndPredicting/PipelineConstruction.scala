package org.csye7200.scala.spark.nbaRatingAndPredicting

import org.apache.spark.ml.feature.{StringIndexer, VectorAssembler}

object PipelineConstruction {

  val MVP_index: StringIndexer = new StringIndexer()
    .setInputCol("Voting")
    .setOutputCol("IndexedVoting")


  // Create the feature columns
  val featureColumns: Array[String] = Array(
    "Year",
    "Age",
    "G",
    "GS",
    "MP",
    "PER",
    "TS%",
    "3PAr",
    "FTr",
    "ORB",
    "DRB%",
    "TRB%",
    "AST%",
    "STL%",
    "BLK%",
    "TOV%",
    "USG%",
    "OWS",
    "DWS",
    "WS",
    "WS/48",
    "OBPM",
    "DBPM",
    "BPM",
    "VORP",
    "FG",
    "FGA",
    "FG",
    "3P",
    "3PA",
    "3P%",
    "2P",
    "2P%",
    "eFG%",
    "FT",
    "FTA",
    "FT%",
    "ORB",
    "DRB",
    "TRB",
    "AST",
    "STL",
    "BLK",
    "TOV"
  )

  // Use the VectorAssembly to create the feature vector
  val vectorFeatureAssembler = new VectorAssembler()
    .setInputCols(featureColumns)
    .setOutputCol("features")


}