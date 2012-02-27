name := "axess"

version := "0.1"

scalaVersion := "2.9.1"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies += "com.typesafe.akka" % "akka-actor" % "2.0-RC2"

libraryDependencies += "org.seleniumhq.selenium" % "selenium-server" % "2.19.0"

libraryDependencies += "fr.javafreelance.fluentlenium" % "fluentlenium-core" % "0.5.3"

seq(Twirl.settings: _*)

