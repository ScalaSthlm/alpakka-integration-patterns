name := "playground"

import Dependencies._

libraryDependencies ++= Seq(
  akkaHttp % Optional,
  "javax.jms" % "jms" % "1.1" % Optional,
  "org.apache.activemq" % "activemq-all" % "5.14.4" % Optional
)