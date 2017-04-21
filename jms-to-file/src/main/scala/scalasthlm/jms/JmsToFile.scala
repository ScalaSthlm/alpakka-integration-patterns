package scalasthlm.jms
// #imports
import java.nio.file.Paths
import java.nio.file.StandardOpenOption._
import javax.jms.{Message, TextMessage}

import akka.NotUsed
import akka.stream.IOResult
import akka.stream.alpakka.jms.JmsSourceSettings
import akka.stream.alpakka.jms.scaladsl.JmsSource
import akka.stream.scaladsl.{FileIO, Sink, Source}
import akka.util.ByteString

import scala.concurrent.Future
import scalasthlm.alpakka.playground.ActiveMqBroker
// #imports

object JmsToFile extends ReadFromJms with App {

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
  // #jms-to-file
  val jmsSource: Source[Message, NotUsed] = // (1)
    JmsSource(
      JmsSourceSettings(connectionFactory).withBufferSize(10).withQueue("test")
    )

  val fileSink: Sink[ByteString, Future[IOResult]] = // (2)
    FileIO.toPath(Paths.get("target/out.txt"),
                  Set(WRITE, TRUNCATE_EXISTING, CREATE))

  val finished: Future[IOResult] =
    jmsSource
      .map(_.asInstanceOf[TextMessage].getText) // (3)
      .map(ByteString(_)) // (4)
      .runWith(fileSink)
  // #jms-to-file
  // format: on
  wait(1)
  for {
    _ <- actorSystem.terminate()
    _ <- ActiveMqBroker.stop()
  } ()

}
