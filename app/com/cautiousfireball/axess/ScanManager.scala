package com.cautiousfireball.axess
import akka.actor.Actor

class ScanManager extends Actor {

  def receive = {
    case "Hello" => sender ! "World from " + self.path.name + "(" + self.path.toString() + ")"
  }

}