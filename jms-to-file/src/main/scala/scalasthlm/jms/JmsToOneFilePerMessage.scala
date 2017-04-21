package scalasthlm.jms

// #imports
import java.nio.file.Paths
import java.nio.file.StandardOpenOption.{CREATE, TRUNCATE_EXISTING, WRITE}
import javax.jms.{Message, TextMessage}

import akka.NotUsed
import akka.stream.alpakka.jms.JmsSourceSettings
import akka.stream.alpakka.jms.scaladsl.JmsSource
import akka.stream.scaladsl.{FileIO, Sink, Source}
import akka.util.ByteString

import scalasthlm.alpakka.playground.ActiveMqBroker
// #imports

object JmsToOneFilePerMessage extends ReadFromJms with App {

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
  // #jms-to-one-file-per-message
  val jmsSource: Source[Message, NotUsed] = // (1)
    JmsSource(
      JmsSourceSettings(connectionFactory).withBufferSize(10).withQueue("test")
    )

  jmsSource
    .map(_.asInstanceOf[TextMessage].getText)               // (2)
    .map(ByteString(_))                                     // (3)
    .zip(Source.fromIterator(() => Iterator.from(0)))       // (4)
    .mapAsync(parallelism = 5) { case (byteStr, number) =>
      Source                                                // (5)
        .single(byteStr)
        .runWith(FileIO.toPath(Paths.get(s"target/out-${number}.txt"),
                               Set(WRITE, TRUNCATE_EXISTING, CREATE)))
    }
    .runWith(Sink.ignore)
  // #jms-to-one-file-per-message
  // format: on
  wait(1)
  for {
    _ <- actorSystem.terminate()
    _ <- ActiveMqBroker.stop()
  } ()

}
