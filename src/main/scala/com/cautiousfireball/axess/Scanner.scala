package com.cautiousfireball.axess
import akka.actor.Actor
import akka.actor.ActorRef

class Scanner(listener: ActorRef) extends Actor {
  def receive = {
    case ScanSite(site) =>
      listener ! ScanResult(site, "Great!")
  }
}