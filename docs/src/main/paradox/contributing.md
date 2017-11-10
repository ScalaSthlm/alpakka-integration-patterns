# Contributing

This pattern repository is curated by the 
Scala Users Group in Stockholm, Sweden.

## Modules

One sbt module should have examples of a common theme.

Start off by creating a copy of the `module-template` module 
and add it to `build.sbt`.

Add the dependencies you need in the module's `build.sbt`. Standard dependencies
are declared in `project/Dependencies.scala`. Declare additional stuff directly 
in `libraryDependencies`.

## Endpoints

To enable working examples we've put a few ready-to-use endpoints in the 
`playground` module. Its dependencies are declared optional, so whenever you 
use parts of it, you'll need to add the required dependencies.

### JMS

You may start an embedded ActiveMQ JMS broker.

Scala
:   ```
      ActiveMqBroker.start()
    ```
    
### HTTP

If you'd like to send data via HTTP, start an embedded Akka Http server as below.
    
Scala
:   ```
      WebServer.start()
    ```

It exposes 

* GET localhost:8080/hello -- replies with a constant JSON snippet
* Web socket localhost:8080/webSocket/ping -- the web socket echos the message a bit wrapped 

## Documentation

This site is generated by [Lightbend's Paradox Markdown Engine](https://developer.lightbend.com/docs/paradox/latest/index.html).

It supports embedding snippets from other files with this notation, where 
`#textmarker` must be present in the denoted file. 

Markdown
:   ```
    @@@snip(..relative/path/to/file) { #textmarker }
    ```

See [Paradox' Sinppet inclusion](https://developer.lightbend.com/docs/paradox/latest/features/snippet-inclusion.html) for details.

As the project applies automatic code formatting with [Scalafmt](http://scalameta.org/scalafmt/), you 
may want to switch it off for snippets used as documentation. 

Scala
: @@snip (../../../../module-template/src/main/scala/scalasthlm/template/Template.scala) { #snippet }

Run `sbt docs/local:paradox` to generate reference docs 
while developing. Generated documentation can be found in the 
`./docs/target/paradox/site/local` directory.