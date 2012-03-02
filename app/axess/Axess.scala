package axess

import akka.actor.Actor
import scala.collection.immutable.Stack
import akka.routing.RoundRobinRouter
import akka.actor.Props

class Axess extends Actor {

  val NUM_MANAGERS = 5

  val managerRouter = context.actorOf(
    Props[ScanManager].withRouter(RoundRobinRouter(NUM_MANAGERS)),
    "managerRouter")

  def receive() = {
    case s @ "Hello" =>
      managerRouter.forward(s)
  }

}