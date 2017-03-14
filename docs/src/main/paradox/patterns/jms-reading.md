# Reading from JMS

### Example: Read text messages from JMS queue and append to file

- listens to the JMS queue "test" receiving `javax.jms.Message`s (1),
- converts the JMS message to a `javax.jms.TextMessage` and extracts the text (3),
- converts incoming data to `akka.util.ByteString` (4),
- and appends the data to the file `target/out` (2).

Scala
: @@snip (../../../../../jms-to-file/src/main/scala/scalasthlm/jms/JmsToFile.scala) { #jms-to-file }


Scala Imports
: @@snip (../../../../../jms-to-file/src/main/scala/scalasthlm/jms/JmsToFile.scala) { #imports }


### Example: Read text messages from JMS queue and send to web server

Scala
: @@snip (../../../../../jms-to-file/src/main/scala/scalasthlm/jms/JmsToHttpGet.scala) { #jms-to-http-get }


Scala Imports
: @@snip (../../../../../jms-to-file/src/main/scala/scalasthlm/jms/JmsToHttpGet.scala) { #imports }

### Example: Read text messages from JMS queue and send to web socket

Scala
: @@snip (../../../../../jms-to-file/src/main/scala/scalasthlm/jms/JmsToWebSocket.scala) { #jms-to-web-socket }


Scala Imports
: @@snip (../../../../../jms-to-file/src/main/scala/scalasthlm/jms/JmsToWebSocket.scala) { #imports }


### Running the example code

This example is contained in a stand-alone runnable main, it can be run
 from `sbt` like this:
 

Scala
:   ```
    sbt
    > jmsToFile/run
    ```
