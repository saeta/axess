package controllers

import play.api._
import play.api.mvc._
import play.api.libs.concurrent._
import akka.actor.ActorSystem
import akka.actor.Actor
import akka.actor.Props
import akka.pattern._
import akka.util.duration._
import akka.util.Timeout
import com.cautiousfireball.axess.Axess

object Application extends Controller {

  class SimpleResponder extends Actor {
    def receive = {
      case "ping" => sender ! "pong"
    }
  }

  val system = ActorSystem("Axess")
  val sr = system.actorOf(Props[SimpleResponder])
  val axess = system.actorOf(Props[Axess])
  implicit val timeout = Timeout(1000 milliseconds)

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def foo = Action {
    AsyncResult {
      (sr ? "ping").mapTo[String].asPromise.map { r => Ok("Response: " + r) }
    }
  }

  def bar = Action {
    AsyncResult {
      (axess ? "Hello").mapTo[String].asPromise.map { r => Ok("Response: " + r) }
    }
  }

}