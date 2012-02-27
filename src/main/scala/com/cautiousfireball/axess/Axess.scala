package com.cautiousfireball.axess

import play.api.mvc.Results._
import play.api._
import play.api.mvc._
import play.api.libs.concurrent._
import akka.actor._
import com.typesafe.play.mini.{ POST, Path, GET, Application }
import html.index

object Axess extends Application {

  def route = {
    case GET(Path("/")) => Action {
      Ok("Hello world @ %s\n".format(System.currentTimeMillis))
    }
    case GET(Path("/index")) => Action {
      val h = html.index("A message!")
      Ok(html.index("A Message!").toString()).as("text/html")
    }
  }

  class Listener extends Actor {
    def receive = {
      case ScanResult(site, result) =>
        println("Site: " + site + " has result: " + result)
        context.system.shutdown()
    }
  }

  scanSite("hello world!")

  def scanSite(site: String) = {
    // Create an Akka system
    val system = ActorSystem("Axess")
    // create a listener to print things out to the console
    val listener = system.actorOf(Props[Listener], name = "listener")

    val scanner = system.actorOf(Props(new Scanner(listener)), name = "scanner")

    // Start scanning
    scanner ! ScanSite(site)
  }

}