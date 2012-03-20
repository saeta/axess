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
  implicit val timeout = Timeout(5000 milliseconds)

  def start(id: Long) = Action {
    Site.getSite(id) match {
      case Some(site) => {
        AsyncResult {
          (axess ? StartScan(id) map {
            case scanStart: ScanStarted =>
              Redirect(routes.ScanController.status(scanStart.scanId))
          } recover {
            case _ => InternalServerError
          }).asPromise
        }
      }
      case None => BadRequest
    }
  }

  def status(id: Long) = Action { implicit request =>
    val started = flash.get("started").getOrElse("n") == "y"
    val scanOption = Scan.getScan(id)
    scanOption match {
      case None => BadRequest
      case Some(scan) =>
        if (scan.finished) {
          Redirect(routes.ScanController.results(id))
        } else {
          AsyncResult {
            (axess ? StatsRequest(id) map {
              case sr: StatsResponse =>
                val s = Site.getSite(scan.siteId).get
                Ok(views.html.scanstats(sr, s, started))
            } recover {
              case _ => Redirect(routes.ScanController.status(id))
            }).asPromise
          }
        }
    }
  }

  def results(id: Long) = Action {
    Scan.getScan(id) match {
      case None => BadRequest("Unknown scan!")
      case Some(scan) =>
        Site.getSite(scan.siteId) match {
          case None => BadRequest("Unknown site!")
          case Some(site) =>
            Ok(views.html.scanResults(site, scan, ScanMsg.numPagesWithIssues(scan.id), ScanMsg.numIssues(scan.id), ScanMsg.allGrouped(scan.id)))
        }
    }
  }

  def summary = Action {
    Ok(views.html.scanHistory(Scan.scanSummaries()))
  }

  def sysstatus = Action {
    AsyncResult {
      (axess ? AxessStatsQuery map {
        case as: AxessStats =>
          Ok(views.html.axessStatus(as, SysStats.get()))
      } recover {
        case _ => InternalServerError
      }).asPromise
    }
  }

}