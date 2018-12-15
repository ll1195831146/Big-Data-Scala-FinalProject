import org.scalatest.{FlatSpec, Matchers}
import DataCleaning.DataCleaning
import RFModel.RandomForest

import org.scalatest.FunSuite

import org.scalatest.FlatSpec


class BadVariableSpec extends FlatSpec {

  "Train and test data" should "not be null" in {
    val train_data = DataCleaning.loadDataset("data/00-17stats.csv")
    val count = train_data.count
    assert(count>0)

  }

  "Predict data" should "not be null" in {
    val test_data = DataCleaning.loadDataset("data/17-18stats.csv")
    val count = test_data.count
    assert(count>0)
  }


  "RMSE" should "lower than 0.05" in {
    val data = DataCleaning.loadDataset("data/00-17stats.csv")
    val dataIndexed = DataCleaning.dataIndexed(data)
    val train_data_final = RandomForest.FeatureDataFrame(dataIndexed)

    val rfmodel = RandomForest.RFModel(train_data_final)

    //Metric--rmse
    val rmse = RandomForest.Metrix(rfmodel,train_data_final)

    assert(rmse<0.05)
  }

}
