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
        |  previewSite - opens the site in web browser
        |    (sbt-site plugin)
        |
        |  docs/ghpagesPushSite - creates the site in gh-pages branch
        |    and pushes it to Github
        |    (sbt-ghpages plugin)
      """.stripMargin
  )

lazy val playground = project
  .in(file("playground"))

lazy val jmsToFile = project
  .in(file("jms-to-file"))
  .dependsOn(playground)

lazy val ftpSamples = project
  .in(file("ftp-samples"))
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
    git.remoteRepo := scmInfo.value.get.connection
  )
