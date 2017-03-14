package scalasthlm.jms

// #imports
import javax.jms

import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.ws.{WebSocketRequest, WebSocketUpgradeResponse}
import akka.stream.alpakka.jms.JmsSourceSettings
import akka.stream.alpakka.jms.scaladsl.JmsSource
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
import akka.{Done, NotUsed}

import scala.collection.immutable.Seq
import scala.concurrent.Future
import scalasthlm.alpakka.playground.{ActiveMqBroker, WebServer}
// #imports

object JmsToWebSocket extends ReadFromJms with App {

  ActiveMqBroker.start()
  WebServer.start()

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

  def wsMessageToString: ws.Message => Future[String] = {
    case message: ws.TextMessage.Strict =>
      Future.successful(message.text)

    case message: ws.TextMessage.Streamed =>
      val seq: Future[Seq[String]] = message.textStream.runWith(Sink.seq)
      seq.map(seq => seq.mkString)

    case message =>
      Future.successful(message.toString)
  }

  // format: off
  // #jms-to-web-socket
  val jmsSource: Source[jms.Message, NotUsed] =
    JmsSource(
      JmsSourceSettings(connectionFactory).withBufferSize(10).withQueue("test")
    )

  val webSocketFlow: Flow[ws.Message, ws.Message, Future[WebSocketUpgradeResponse]] =
    Http().webSocketClientFlow(WebSocketRequest("ws://localhost:8080/webSocket/ping"))

  val (wsUpgradeResponse, finished): (Future[WebSocketUpgradeResponse], Future[Done]) =
    jmsSource
      .map(_.asInstanceOf[jms.TextMessage].getText)
      .map(ws.TextMessage(_))
      .viaMat(webSocketFlow)(Keep.right)
      .mapAsync(1)(wsMessageToString)
      .map("client received: " + _)
      .toMat(Sink.foreach(println))(Keep.both)
      .run()
  // #jms-to-web-socket
  // format: on

  wsUpgradeResponse
    .map { upgrade =>
      if (upgrade.response.status == StatusCodes.SwitchingProtocols) {
        "WebSocket established"
      } else {
        throw new RuntimeException(
          s"Connection failed: ${upgrade.response.status}")
      }
    }
    .onComplete(println)

  finished.foreach(_ => println("stream finished"))

  for {
    _ <- actorSystem.terminate()
    _ <- WebServer.stop()
    _ <- ActiveMqBroker.stop()
  } ()

}
