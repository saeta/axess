import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "axess"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      // Add your project dependencies here,
      "org.seleniumhq.selenium" % "selenium-server" % "2.19.0"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
      // Add your own project settings here      
    )

}
