package scalasthlm.ftpsamples
// #imports
import java.io.File
import java.net.InetAddress
import java.nio.file.{FileSystem, Files}

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.alpakka.ftp.RemoteFileSettings._
import akka.stream.alpakka.ftp._
import akka.stream.alpakka.ftp.scaladsl.Ftp
import akka.stream.scaladsl.{Sink, Source}
import com.google.common.jimfs.{Configuration, Jimfs}
import org.apache.mina.util.AvailablePortFinder

import scala.concurrent.duration.DurationInt
import scalasthlm.alpakka.playground.FtpServerEmbedded
import scalasthlm.alpakka.playground.filesystem.FileSystemMock
// #imports

object FtpToFile extends App {

  implicit val actorSystem = ActorSystem()
  implicit val actorMaterializer = ActorMaterializer()
  implicit val executionContext = actorSystem.dispatcher

  def main(): Unit = {
    val ftpFileSystem = new FileSystemMock()

    val port = AvailablePortFinder.getNextAvailable(21000)
    val ftpServer = FtpServerEmbedded.start(ftpFileSystem.fileSystem, port)
    //
    ftpFileSystem.generateFiles(30, 10, "/home/anonymous")
    ftpFileSystem.putFileOnFtp("/home/anonymous", "hello.txt")
    ftpFileSystem.putFileOnFtp("/home/anonymous", "hello2.txt")

    val settings = FtpSettings(
      InetAddress.getByName("localhost"),
      port
    )

    // fo rmat: off
    // #list-files
    Ftp
      .ls("/", settings)
      .zip(Source.fromIterator(() => Iterator.from(1)))
      .filter { case (file, _) => file.isFile }
      .mapAsyncUnordered(parallelism = 5) {
        case (file, index) =>
          Source
            .single(s"file: ${file.name}")
            .concat {
              Ftp
                .fromPath(ftpFileSystem.fileSystem.getPath(file.path), settings)
                .map(_.utf8String)
            }
            .runForeach(println)
      }
      .runWith(Sink.ignore)
    // #list-files
    // fo rmat: on

    Thread.sleep(30.seconds.toMillis)
    actorSystem.terminate().onComplete { _ =>
      ftpServer.stop()
    }
  }

  main()

}
