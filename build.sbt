lazy val alpakkaIntegrationPatterns = project
  .in(file("."))
  .aggregate(
    jmsToFile,
    ftpSamples
  )
  .settings(
    onLoadMessage :=
      """
        |** Welcome to Alpakka Integration patterns! **
        |
        |Useful sbt tasks:
        |
        |  docs/paradox - builds documentation with locally
        |    linked Scala API docs, which can be found at
        |    docs/target/paradox/site/main
      """.stripMargin
  )

lazy val playground = project
  .in(file("playground"))
  .enablePlugins(AutomateHeaderPlugin)

lazy val jmsToFile = project
  .in(file("jms-to-file"))
  .enablePlugins(AutomateHeaderPlugin)
  .dependsOn(playground)

lazy val ftpSamples = project
  .in(file("ftp-samples"))
  .enablePlugins(AutomateHeaderPlugin)
  .dependsOn(playground)

val Local = config("local")
val defaultParadoxSettings: Seq[Setting[_]] = Seq(
  paradoxTheme := Some(builtinParadoxTheme("generic")),
  paradoxProperties ++= Map(
    "version" -> version.value,
    "AkkaVersion" -> Dependencies.AkkaVersion,
    "AkkaHttpVersion" -> Dependencies.AkkaHttpVersion,
    "AlpakkaVersion" -> Dependencies.AlpakkaVersion,
    "scala.binaryVersion" -> scalaBinaryVersion.value,
    "extref.akka-docs.base_url" -> s"http://doc.akka.io/docs/akka/${Dependencies.AkkaVersion}/%s",
    "extref.akka-http-docs.base_url" -> s"http://doc.akka.io/docs/akka-http/${Dependencies.AkkaHttpVersion}/%s",
    "extref.alpakka-docs.base_url" -> s"http://developer.lightbend.com/docs/alpakka/${Dependencies.AlpakkaVersion}/%s",
    "extref.java-api.base_url" -> "https://docs.oracle.com/javase/8/docs/api/index.html?%s.html",
    "extref.paho-api.base_url" -> "https://www.eclipse.org/paho/files/javadoc/index.html?%s.html",
    "scaladoc.akka.base_url" -> s"http://doc.akka.io/api/akka/${Dependencies.AkkaVersion}",
    "scaladoc.akka.stream.alpakka.base_url" -> s"http://developer.lightbend.com/docs/api/alpakka/${version.value}"
  ),
  sourceDirectory := baseDirectory.value / "src" / "main"
)

lazy val docs = project
  .enablePlugins(ParadoxPlugin, ParadoxSitePlugin, GhpagesPlugin)
  .settings(
    name := "Alpakka Integration Patterns",
    defaultParadoxSettings,
    scmInfo := Some(
      ScmInfo(
        url("https://github.com/ScalaSthlm/alpakka-integration-patterns.git"),
        "git@github.com:ScalaSthlm/alpakka-integration-patterns.git")),
    git.remoteRepo := scmInfo.value.get.connection
  )
