name := "csv-samples"

/*
  To uses this sbt project stand-alone,
  copy ../project/Dependencies.scala into ./project.
 */
import Dependencies._

libraryDependencies ++= Seq(
  alpakkaCsv,
  akkaHttp,
  akkaHttpSprayJson
)
