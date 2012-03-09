package controllers

import play.api.mvc._
import play.api._
import play.api.data._
import play.api.data.Forms._
import models._
import akka.actor._
import axess._
import akka.util.duration._
import akka.util.Timeout
import akka.pattern._
import axess._
import play.api.libs.concurrent._
import play.api.Play._

object ScanController extends Controller {

  val system = ActorSystem("axess")
  val axess = system.actorOf(Props[Axess], "axess")
  implicit val timeout = Timeout(1000 milliseconds)

  def start(id: Long) = Action {
    Site.getSite(id) match {
      case Some(site) => {
        if (!site.scannable) {
          BadRequest
        } else {
          AsyncResult {
            (axess ? StartScan(id)).mapTo[ScanStarted].asPromise.map {
              scanStart =>
                Redirect(
                  routes.ScanController.status(scanStart.scanId)).flashing(
                    "started" -> "y")
            }
          }
        }
      }
      case None => BadRequest
    }
  }

  def status(id: Long) = Action { implicit request =>
    val started = flash.get("started").getOrElse("n") == "y"
    if (started) {
      Ok("Scan Started")
    } else {
      Ok("Scan not started...")
    }
  }

  def statusApi(id: Long) = TODO

  def results(id: Long) = Action {
    Ok("Scan results!")
  }

}