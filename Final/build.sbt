name := "NBA_MVP_Prediction"

scalaVersion := "2.11.12"

libraryDependencies ++= Seq(
  "org.apache.spark" % "spark-core_2.11" % "2.2.0",
  "org.apache.spark" % "spark-sql_2.11" % "2.2.0",
  "org.apache.spark" % "spark-mllib_2.11" % "2.2.0",
  "org.apache.spark" % "spark-graphx_2.11" % "2.2.0",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "org.apache.spark" % "spark-hive_2.10" % "1.5.1",
  "junit" % "junit" % "4.12" % "test"
)

