import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "axess"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      // Add your project dependencies here,
      "org.seleniumhq.selenium" % "selenium-server" % "2.19.0",
      "mysql" % "mysql-connector-java" % "5.1.18",
      "org.apache.httpcomponents" % "httpclient" % "4.1.3",
      "org.clapper" %% "classutil" % "0.4.4"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
      // Add your own project settings here      
    )

}
