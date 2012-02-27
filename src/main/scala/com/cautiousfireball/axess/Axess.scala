package com.cautiousfireball.axess

import akka.actor._

object Axess extends App {

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