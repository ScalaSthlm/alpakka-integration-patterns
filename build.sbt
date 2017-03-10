lazy val alpakkaIntegrationPatterns = project
  .in(file("."))
  .enablePlugins(PublishUnidoc)
  .aggregate()

lazy val jmsToFile = project.in(file("jms-to-file"))
  .enablePlugins(AutomateHeaderPlugin)

val Local = config("local")
val defaultParadoxSettings: Seq[Setting[_]] = Seq(
  paradoxTheme := Some(builtinParadoxTheme("generic")),
  paradoxProperties ++= Map(
    "version" -> version.value,
    "AkkaVersion" -> Dependencies.AkkaVersion,
    "AkkaHttpVersion" -> Dependencies.AkkaHttpVersion,
    "AlpakkaVersion" -> Dependencies.AlpakkaVersion,
    "scala.binaryVersion" -> scalaBinaryVersion.value,
    "extref.akka-docs.base_url" -> s"http://doc.akka.io/docs/akka/${Dependencies.AkkaVersion}/%s.html",
    "extref.alpakka-docs.base_url" -> s"http://developer.lightbend.com/docs/api/alpakka/${Dependencies.AlpakkaVersion}/akka/stream/alpakka/%s.html",
    "extref.java-api.base_url" -> "https://docs.oracle.com/javase/8/docs/api/index.html?%s.html",
    "extref.paho-api.base_url" -> "https://www.eclipse.org/paho/files/javadoc/index.html?%s.html",
    "scaladoc.akka.base_url" -> s"http://doc.akka.io/api/akka/${Dependencies.AkkaVersion}",
    "scaladoc.akka.stream.alpakka.base_url" -> s"http://developer.lightbend.com/docs/api/alpakka/${version.value}"
  ),
  sourceDirectory := baseDirectory.value / "src" / "main"
)

lazy val docs = project
  .enablePlugins(ParadoxPlugin, NoPublish)
  //.disablePlugins(BintrayPlugin)
  .settings(
    name := "Alpakka Integration Patterns",
    inConfig(Compile)(defaultParadoxSettings),
    ParadoxPlugin.paradoxSettings(Local),
    inConfig(Local)(defaultParadoxSettings),
    paradoxProperties in Local ++= Map(
      // point API doc links to locally generated API docs
    /*  "scaladoc.akka.stream.alpakka.base_url" -> rebase(
        (baseDirectory in alpakka).value, "../../../../../"
      )((sbtunidoc.Plugin.UnidocKeys.unidoc in alpakka in Compile).value.head).get
*/
    )
  )
