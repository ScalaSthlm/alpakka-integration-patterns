package scalasthlm.jms
// #imports
import java.nio.file.Paths

import akka.NotUsed
import akka.stream.IOResult
import akka.stream.alpakka.jms.JmsSourceSettings
import akka.stream.alpakka.jms.scaladsl.JmsSource
import akka.stream.scaladsl.{FileIO, Sink, Source}
import akka.util.ByteString

import scala.concurrent.Future
// #imports

object Template extends BaseTrait with App {

  // format: off
  // #sampleSnippet1
  println("this is an example")
  // #sampleSnippet1
  // format: on
  wait(1)
  for {
    _ <- actorSystem.terminate()
    _ <- ActiveMqBroker.stop()
  } ()

  def sampleUsedInContributingMd: Unit = {
    // #snippet
    // format: off
    // # snippet-marker
    val httpConnectionFlow: Flow[HttpRequest, HttpResponse, Future[Http.OutgoingConnection]] =
      Http().outgoingConnection("localhost", 8080)
    // # snipper-marker
    // format: on
    // #snippet
  }

}
