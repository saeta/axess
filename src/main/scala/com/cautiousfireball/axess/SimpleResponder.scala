package com.cautiousfireball.axess
import akka.actor.Actor

class SimpleResponder extends Actor {

  def receive = {
    case "ping" => sender ! "pong"
  }
}