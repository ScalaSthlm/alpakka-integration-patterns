package scalasthlm.ftpsamples
// #imports
import java.net.InetAddress
import java.nio.file.{Files, Paths}

import akka.actor.ActorSystem
import akka.stream.alpakka.ftp.RemoteFileSettings._
import akka.stream.alpakka.ftp.scaladsl.Ftp
import akka.stream.scaladsl.{FileIO, Sink}
import akka.stream.{ActorMaterializer, IOResult}
import org.apache.mina.util.AvailablePortFinder

import scala.collection.immutable
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.util.{Failure, Success}
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

    val ftpSettings = FtpSettings(
      InetAddress.getByName("localhost"),
      port
    )

    val targetDir = Paths.get("target/")
    // for mat: off
    // #fetch-files
    val fetchedFiles: Future[immutable.Seq[(String, IOResult)]] =
      Ftp
        .ls("/", ftpSettings)
        .filter(ftpFile => ftpFile.isFile)
        .mapAsyncUnordered(parallelism = 5) { ftpFile =>
          val localPath = targetDir.resolve("." + ftpFile.path)
          val dir = Files.createDirectories(localPath.getParent)
          val future = Ftp
            .fromPath(ftpFile.path, ftpSettings)
            .runWith(FileIO.toPath(localPath))
          future.map { ioResult =>
            (ftpFile.path, ioResult)
          }
        }
        .runWith(Sink.seq)
    // #fetch-files
    // format: on
    fetchedFiles
      .map { files =>
        files.filter { case (_, r) => !r.wasSuccessful }
      }
      .onComplete {
        case Success(errors) if errors.isEmpty =>
          println("all files fetched.")
        case Success(errors) =>
          println(s"errors occured: ${errors.mkString("\n")}")
        case Failure(exception) =>
          println("The stream failed")
      }
    Thread.sleep(10.seconds.toMillis)
    actorSystem.terminate().onComplete { _ =>
      ftpServer.stop()
    }
  }

  main()

}
