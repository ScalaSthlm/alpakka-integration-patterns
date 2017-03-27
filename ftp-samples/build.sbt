name := "ftp-samples"

/*
  To uses this sbt project stand-alone,
  copy ../project/Dependencies.scala into ./project.
 */
import Dependencies._

libraryDependencies ++= Seq(
  apacheFtp,
  akkaStream,
  alpakkaFtp,
  "com.google.jimfs" % "jimfs" % "1.1" // ApacheV2
) ++
  Dependencies.logging
