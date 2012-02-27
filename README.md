<!--- Build this file by running `pandoc README.md > README.html` -->
# Axess: A website accessibility checker #

 * Author: Brennan Saeta <brennan.saeta@gmail.com>
 * Written: February, 2012
 * Status: Bleeding
 * Contributors:
     - Your name here

## Background ##

This winter, I volunteered (for school credit) with [Coursera][coursera].
This tool was initially written (in Java) to help them test their class pages
to ensure they maintained a standard of accessibility. They liked it so much,
they wanted me to (1) open source it, and (2) put a web interface on it to make
it self-service for professors and others to run. Because I had initially
written it in Java using Guice, it was nice and modular, but would have
required a substantial rewrite to convert it to a web environment. Thus this
project was born.

[coursera]: http://www.coursera.org/ "Coursera"

### Choice of tools ###

I chose to build this tool on top of the Typesafe stack mostly out of
curiosity. That and when I read through the Play documentation, I really
liked the Selenium API. (It turns out, that API isn't part of the Typesafe
stack, but the first reason applies nonetheless. :-D)

### Choice of Name ##

Stanford has a really bad administrative website called [Axess][axess].

[axess]:http://axess.stanford.edu "Axess - Stanford's Administrative Portal"

# Developing #

Pull requests and patches are always welcome! Follow the instructions below
to help set up your workspace and development environment. Please follow the
source code style conventions.

## Prereq's ##

You will need [`sbt`][sbt] to compile and run this project.

[sbt]: https://github.com/harrah/xsbt/wiki "Simple Build Tool"

## Running a test application ##

Thanks to `sbt`, compiling and running your own version of `Axess` is easy.
Just:

    sbt compile
    sbt run

> Note: `sbt run` will compile for you.

By default, this will start a web browser on port 9000. (TODO document how to
change this).

## Eclipse Configuration ##

You can generate the Eclipse `.project` and `.classpath` files by running
`sbt eclipse`. 

You may now import the project normally. (In Eclipse: File > Import ... Accept
all the defaults. Everything should work. Mostly.)

This will likely result in compile errors due to an unfortunate interaction
between [Twirl][twirl] and [sbteclipse][sbteclipse]. To fix this:

1. In Eclipse, find the folder `target/scala-2.9.1/src_managed/main/generated-twirl-sources`
2. Right-click on that folder, and select Build Path > Use as Source Folder
3. Clean Eclipse (Project > Clean... Just clean everything.)

[twirl]: https://github.com/spray/twirl "The Play framework Scala template engine"
[sbteclipse]: https://github.com/typesafehub/sbteclipse "sbt plugin to create Eclipse project definitions"

