name := "jms-to-file"

import Dependencies._

libraryDependencies ++= Seq(
  alpakkaJms,
  "javax.jms" % "jms" % "1.1",
  "org.apache.activemq" % "activemq-all" % "5.14.4"
)