package scalasthlm.jms

// #imports
import javax.jms.{ConnectionFactory, Message, TextMessage}

import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.stream.alpakka.jms.JmsSourceSettings
import akka.stream.alpakka.jms.scaladsl.JmsSource
import akka.stream.scaladsl.{Flow, Sink, Source}
import akka.util.ByteString
import akka.{Done, NotUsed}

import scala.concurrent.Future
import scalasthlm.alpakka.playground.{ActiveMqBroker, WebServer}
// #imports

object JmsToHttpGet extends ReadFromJms with App {

  WebServer.start()
  ActiveMqBroker.start()

  val connectionFactory = ActiveMqBroker.createConnectionFactory
  enqueue(connectionFactory)("a",
                             "b",
                             "c",
                             "d",
                             "e",
                             "f",
                             "g",
                             "h",
                             "i",
                             "j",
                             "k")

  // format: off
  // #jms-to-http-get
  val jmsSource: Source[Message, NotUsed] = // (1)
    JmsSource(
      JmsSourceSettings(connectionFactory).withBufferSize(10).withQueue("test")
    )

  val httpConnectionFlow: Flow[HttpRequest, HttpResponse, Future[Http.OutgoingConnection]] = // (2)
    Http().outgoingConnection("localhost", 8080)

  val finished: Future[Done] =
    jmsSource
      .map(_.asInstanceOf[TextMessage].getText) // (3)
      .map(ByteString(_)) // (4)
      .map(bs => HttpRequest(uri = Uri("/hello"), entity = HttpEntity(bs))) // (5)
      .via(httpConnectionFlow) // (6)
      .runWith(Sink.foreach(println))
  // #jms-to-http-get
  // format: on
  finished.foreach(_ => println("stream finished"))

  wait(10)
  for {
    _ <- actorSystem.terminate()
    _ <- WebServer.stop()
    _ <- ActiveMqBroker.stop()
  } ()

}
