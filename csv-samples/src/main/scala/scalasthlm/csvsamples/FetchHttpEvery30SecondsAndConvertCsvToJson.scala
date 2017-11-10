package scalasthlm.csvsamples
// #imports
import akka.http.scaladsl._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.stream.alpakka.csv.scaladsl.{CsvParsing, CsvToMap}
import akka.stream.scaladsl.{Keep, Sink, Source}
import akka.util.ByteString
import spray.json.{DefaultJsonProtocol, JsValue, JsonWriter}

import scala.concurrent.duration.DurationInt
// #imports

object FetchHttpEvery30SecondsAndConvertCsvToJson
    extends BaseTrait
    with App
    with DefaultJsonProtocol {

  // format: off
  // #helper
  val httpRequest = HttpRequest(uri = "http://www.nasdaq.com/screening/companies-by-name.aspx?exchange=NASDAQ&render=download")

  def extractEntityData(response: HttpResponse): Source[ByteString, _] =
    response match {
      case HttpResponse(OK, _, entity, _) => entity.dataBytes
      case notOkResponse =>
        Source.failed(new RuntimeException(s"illegal response $notOkResponse"))
    }

  def cleanseCsvData(csvData: Map[String, ByteString]): Map[String, String] =
    csvData
      .filterNot { case (key, _) => key.isEmpty }
      .mapValues(_.utf8String)

  def toJson(map: Map[String, String])(
      implicit jsWriter: JsonWriter[Map[String, String]]): JsValue = jsWriter.write(map)
  // #helper
  // format: on

  val (ticks, future) =
    // format: off
    // #sample
    Source                                              // stream element type
      .tick(1.seconds, 30.seconds, httpRequest)         //: HttpRequest (1)
      .mapAsync(1)(Http().singleRequest(_))             //: HttpResponse (2)
      .flatMapConcat(extractEntityData)                 //: ByteString (3)
      .via(CsvParsing.lineScanner())                    //: List[ByteString] (4)
      .via(CsvToMap.toMap())                            //: Map[String, ByteString] (5)
      .map(cleanseCsvData)                              //: Map[String, String] (6)
      .map(toJson)                                      //: JsValue (7)
      // #sample
      // format: on
      .map(_.prettyPrint)
      .toMat(Sink.foreach(println))(Keep.both)
      .run()

  wait(1.minutes)
  ticks.cancel()
  terminateActorSystem()
}
