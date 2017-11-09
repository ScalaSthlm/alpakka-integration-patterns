package scalasthlm.ftpsamples
// #imports
import java.net.InetAddress
import java.nio.file.{Files, Paths}

import akka.actor.ActorSystem
import akka.stream.alpakka.ftp.FtpSettings
import akka.stream.alpakka.ftp.scaladsl.Ftp
import akka.stream.scaladsl.{FileIO, Sink}
import akka.stream.{ActorMaterializer, IOResult}
import org.apache.mina.util.AvailablePortFinder

import scala.collection.immutable
import scala.concurrent.Future
import scala.util.{Failure, Success}
import scalasthlm.alpakka.playground.FtpServerEmbedded
import scalasthlm.alpakka.playground.filesystem.FileSystemMock
// #imports

object FtpToFile extends App {

  implicit val actorSystem = ActorSystem()
  implicit val actorMaterializer = ActorMaterializer()
  implicit val executionContext = actorSystem.dispatcher

  val ftpFileSystem = new FileSystemMock()

  val port = AvailablePortFinder.getNextAvailable(21000)
  val ftpServer = FtpServerEmbedded.start(ftpFileSystem.fileSystem, port)
  //
  ftpFileSystem.generateFiles(30, 10, "/home/anonymous")
  ftpFileSystem.putFileOnFtp("/home/anonymous", "hello.txt")
  ftpFileSystem.putFileOnFtp("/home/anonymous", "hello2.txt")

  // #fetch-files
  val ftpSettings = FtpSettings(InetAddress.getByName("localhost"), port)

  // #fetch-files

  val targetDir = Paths.get("target/")
  val fetchedFiles: Future[immutable.Seq[(String, IOResult)]] =
    // format: off
  // #fetch-files
    Ftp
      .ls("/", ftpSettings)                                    // (1)
      .filter(ftpFile => ftpFile.isFile)                       // (2)
      .mapAsyncUnordered(parallelism = 5) { ftpFile =>         // (3)
        val localPath = targetDir.resolve("." + ftpFile.path)
        val dir = Files.createDirectories(localPath.getParent)
        val fetchFile: Future[IOResult] = Ftp
          .fromPath(ftpFile.path, ftpSettings)                
          .runWith(FileIO.toPath(localPath))                   // (4)
        fetchFile.map { ioResult =>                            // (5)
          (ftpFile.path, ioResult)
        }
      }
      .runWith(Sink.seq)                                       // (6)
  // #fetch-files
  // format: on
  fetchedFiles
    .map { files =>
      files.filter { case (_, r) => !r.wasSuccessful }
    }
    .onComplete { res =>
      res match {
        case Success(errors) if errors.isEmpty =>
          println("all files fetched.")
        case Success(errors) =>
          println(s"errors occured: ${errors.mkString("\n")}")
        case Failure(exception) =>
          println("The stream failed")
      }
      actorSystem.terminate().onComplete { _ =>
        ftpServer.stop()
      }
    }
}
