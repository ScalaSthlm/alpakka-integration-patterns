# FTP samples

### Example: Copy all files from an FTP server to local files

- list FTP server contents (1),
- just bother about file entries (2),
- for each file prepare for awaiting `Future` results ignoring the stream order (3),
- run a new stream copying the file contents to a local file (4),
- combine the filename and the copying result (5),
- collect all filenames with results into a sequence (6)

Scala
: @@snip (../../../../../ftp-samples/src/main/scala/scalasthlm/ftpsamples/FtpToFile.scala) { #fetch-files }


Scala Imports
: @@snip (../../../../../ftp-samples/src/main/scala/scalasthlm/ftpsamples/FtpToFile.scala) { #imports }


### Running the example code

This example is contained in a stand-alone runnable main, it can be run
 from `sbt` like this:
 

Scala
:   ```
    sbt
    > ftpSamples/run
    ```
