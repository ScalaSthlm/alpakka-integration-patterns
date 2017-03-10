# JMS to File

### Example 

- listens to the JMS queue "test" receiving `javax.jms.Message`s (1),
- converts the JMS message to a `javax.jms.TextMessage` and extracts the text (3),
- converts incoming data to `akka.util.ByteString` (4),
- and appends the data to the file `target/out` (2).

Scala
: @@snip (../../../../jms-to-file/src/main/scala/scalasthlm/jmstofile/JmsToFile.scala) { #jms-to-file }

### Imports

The sample code uses these imports:

Scala
: @@snip (../../../../jms-to-file/src/main/scala/scalasthlm/jmstofile/JmsToFile.scala) { #imports }


### Running the example code

This example is contained in a stand-alone runnable main, it can be run
 from `sbt` like this:
 

Scala
:   ```
    sbt
    > jmsToFile/run
    ```
