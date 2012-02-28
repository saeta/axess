package com.cautiousfireball.axess

import akka.actor.Actor
import akka.pattern._
import akka.util.duration._
import akka.util.Timeout

class ScanManager extends Actor {

  override def preStart() = {
    val provider = context.actorFor("driverProvider")
    // ... Set up the everything and everything
  }

  def receive = {
    case "Hello" => sender ! "World from " + self.path.name + "(" + self.path.toString() + ")"
  }

}