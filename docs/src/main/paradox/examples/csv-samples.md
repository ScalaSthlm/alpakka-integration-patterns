# CSV samples

### Example: Fetch CSV from Internet and publish the data as JSON

This example uses 
@extref[Akka HTTP to send the HTTP request](akka-http-docs:scala/http/client-side/connection-level.html#opening-http-connections) 
and Akka HTTPs primary JSON support
via @extref[Spray JSON](akka-http-docs:scala/http/common/json-support.html#spray-json-support) 
to convert the map into a JSON structure. 

- (1) trigger an HTTP request every 30 seconds,
- (2) send it to web server,
- (3) continue with the response body as a stream of `ByteString`,
- (4) scan the stream for CSV lines,
- (5) convert the CSV lines into maps with the header line as keys,
- (6) local logic to clean the data and convert values to Strings,
- (7) convert the maps to JSON with Spray JSON from Akka HTTP

Scala
: @@snip (../../../../../csv-samples/src/main/scala/scalasthlm/csvsamples/FetchHttpEvery30SecondsAndConvertCsvToJson.scala) { #sample }

### Helper code

Scala
: @@snip (../../../../../csv-samples/src/main/scala/scalasthlm/csvsamples/FetchHttpEvery30SecondsAndConvertCsvToJson.scala) { #helper }

### Imports

Scala
: @@snip (../../../../../csv-samples/src/main/scala/scalasthlm/csvsamples/FetchHttpEvery30SecondsAndConvertCsvToJson.scala) { #imports }


### Running the example code

This example is contained in a stand-alone runnable main, it can be run
 from `sbt` like this:
 

Scala
:   ```
    sbt
    > csvSamples/run
    ```
