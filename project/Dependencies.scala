import sbt._, Keys._

object Dependencies {

  val ScalaVersions = Seq("2.11.8", "2.12.1")
  val AkkaVersion = "2.4.17"
  val AkkaHttpVersion = "10.0.3"
  val AlpakkaVersion = "0.6"

  val akkaStream = "com.typesafe.akka" %% "akka-stream" % AkkaVersion
  val akkaStramTestkit = "com.typesafe.akka" %% "akka-stream-testkit" % AkkaVersion

  val akkaHttp = "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion
  val akkaHttpXml = "com.typesafe.akka" %% "akka-http-xml" % AkkaHttpVersion

  val scalaTest = "org.scalatest" %% "scalatest" % "3.0.1" % Test // ApacheV2
  val junitInterface = "com.novocode" % "junit-interface" % "0.11" % Test // BSD-style
  val junit = "junit" % "junit" % "4.12" % Test // Eclipse Public License 1.0

  val alpakkaJms = "com.lightbend.akka" %% "akka-stream-alpakka-jms" % AlpakkaVersion
  val alpakkaFiles = "com.lightbend.akka" %% "akka-stream-alpakka-file" % AlpakkaVersion
}
