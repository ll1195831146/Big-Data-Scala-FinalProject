
import SparkConfig.SparkConfig
import DataCleaning.DataCleaning
import RFModel.RandomForest
import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.{Pipeline, PipelineModel}
import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.ml.regression.{RandomForestRegressionModel, RandomForestRegressor}
import org.apache.spark.sql.SparkSession

object PredictingMVP {
  def main(args: Array[String]):Unit = {

    val data = DataCleaning.loadDataset("data/00-17stats.csv")
    val dataIndexed = DataCleaning.dataIndexed(data)
    val train_data_final = RandomForest.FeatureDataFrame(dataIndexed)
    val rfmodel = RandomForest.RFModel(train_data_final)

    //Metrix--rmse
    val rmse = RandomForest.Metrix(rfmodel,train_data_final)

    //Prediction
    val pred_data = DataCleaning.loadDataset("data/17-18stats.csv")
    val pred_data_threshhold = DataCleaning.threshhold(pred_data)
    val pred_data_indexed = DataCleaning.dataIndexed(pred_data_threshhold)

    val pred_data_final = RandomForest.FeatureDataFrame(pred_data_indexed)




    // Make predictions.
    val predictions = RandomForest.prediction(rfmodel, pred_data_final)
  }
}