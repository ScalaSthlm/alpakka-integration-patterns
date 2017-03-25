package scalasthlm.jms

import javax.jms.ConnectionFactory

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.alpakka.jms.JmsSinkSettings
import akka.stream.alpakka.jms.scaladsl.JmsSink
import akka.stream.scaladsl.{Sink, Source}

class BaseTrait {
  implicit val actorSystem = ActorSystem()
  implicit val actorMaterializer = ActorMaterializer()
  implicit val executionContext = actorSystem.dispatcher

  def wait(seconds: Int) = Thread.sleep(seconds * 1000)
}
