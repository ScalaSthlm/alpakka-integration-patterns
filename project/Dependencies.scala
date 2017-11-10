import sbt._, Keys._

object Dependencies {

  val ScalaVersions = Seq("2.11.11", "2.12.4")
  val AkkaVersion = "2.5.6"
  val AkkaHttpVersion = "10.0.10"
  val AlpakkaVersion = "0.14"
  val AkkaKafkaVersion = "0.17"

  val akkaStream = "com.typesafe.akka" %% "akka-stream" % AkkaVersion
  val akkaStreamTestkit = "com.typesafe.akka" %% "akka-stream-testkit" % AkkaVersion

  val akkaHttp = "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion
  val akkaHttpXml = "com.typesafe.akka" %% "akka-http-xml" % AkkaHttpVersion
  val akkaHttpSprayJson = "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion

  val scalaTest = "org.scalatest" %% "scalatest" % "3.0.1" % Test // ApacheV2
  val junitInterface = "com.novocode" % "junit-interface" % "0.11" % Test // BSD-style
  val junit = "junit" % "junit" % "4.12" % Test // Eclipse Public License 1.0

  val alpakkaCsv = "com.lightbend.akka" %% "akka-stream-alpakka-csv" % AlpakkaVersion
  val alpakkaJms = "com.lightbend.akka" %% "akka-stream-alpakka-jms" % AlpakkaVersion
  val alpakkaFiles = "com.lightbend.akka" %% "akka-stream-alpakka-file" % AlpakkaVersion
  val alpakkaFtp = "com.lightbend.akka" %% "akka-stream-alpakka-ftp" % AlpakkaVersion

  val akkaKafka = "com.typesafe.akka" %% "akka-stream-kafka" % AkkaKafkaVersion

  // https://github.com/manub/scalatest-embedded-kafka/tags
  val embeddedKafka = "net.manub" %% "scalatest-embedded-kafka" % "1.0.0"


  // https://mina.apache.org/ftpserver-project/downloads.html
  val apacheFtp = "org.apache.ftpserver" % "ftpserver-core" % "1.1.1"

  val logging = Seq(
    "org.slf4j" % "log4j-over-slf4j" % "1.7.21", // MIT like: http://www.slf4j.org/license.html
    "org.slf4j" % "slf4j-api" % "1.7.21", // MIT like: http://www.slf4j.org/license.html
    "ch.qos.logback" % "logback-classic" % "1.1.7", // Eclipse Public License 1.0: http://logback.qos.ch/license.html
    "ch.qos.logback" % "logback-core" % "1.1.7" // Eclipse Public License 1.0: http://logback.qos.ch/license.html
  )
}
