
import SparkConfig.SparkConfig
import DataCleaning.DataCleaning
import RFModel.RandomForest
import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.{Pipeline, PipelineModel}
import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.ml.regression.{RandomForestRegressionModel, RandomForestRegressor}
import org.apache.spark.sql.SparkSession
import org.apache.spark.ml.feature.{StringIndexer, VectorAssembler}
import org.apache.spark.ml.feature.VectorIndexer

object PredictingMVP {
  def main(args: Array[String]) = {
    //
    //
    //    // loading the training data set
    //    val trainDataset = DataCleaning.loadTrainDataset(hiveContext, args(0))
    //
    //    // loading the test data set
    //    val Array(testRawdata, testDataset) = DataAggregatorUtil.loadTestDataset(hiveContext, args(1))
    //
    //    //  Preparing the Spark Random Forest Pipeline for using ML library
    //    val randomForestTvs = RandomForest.prepareRandomForestPipeline()
    //
    //    //Preparing the Random Forest Model
    //    val randomForestModel = RandomForest.prepareAndFitModel(randomForestTvs, trainDataset)
    //
    //    //Transforming the test data set according to the model
    //    val randomForestOutput = randomForestModel.transform(testDataset)
    //      .withColumnRenamed("prediction", "Sales")
    //      .withColumnRenamed("Id", "PredId")
    //      .select("PredId", "Sales")
    //
    //    SalesPredictionUsageUtil.savePredictions(randomForestOutput, testRawdata, SALES_PREDICTION_RESULT_CSV)
    //
    //  }
    //Train Model
    val data = DataCleaning.loadDataset("data/00-17stats.csv")
    val dataIndexed = DataCleaning.dataIndexed(data)
    val train_data_final = RandomForest.FeatureDataFrame(dataIndexed)
    val rfmodel = RandomForest.RFModel(train_data_final)

//    val evaluator = RandomForest.Metrix(model,dataIndexed)
//    evaluator

    //Prediction
    val pred_data = DataCleaning.loadDataset("data/17-18stats.csv")
    val pred_data_indexed = DataCleaning.dataIndexed(pred_data)

    val pred_data_final = RandomForest.FeatureDataFrame(pred_data_indexed)

    // Make predictions.
    val predictions = RandomForest.prediction(rfmodel, pred_data_final)
  }
}