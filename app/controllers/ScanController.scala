// Copyright 2012 Brennan Saeta
//
// This file is part of Axess
//
// Axess is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// Axess is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with Axess.  If not, see <http://www.gnu.org/licenses/>.

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
  implicit val timeout = Timeout(10 seconds)

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
            (axess ? StatsRequest(id) map { a: Any =>
              a match {
                case sr: StatsResponse =>
                  val s = Site.getSite(scan.siteId).get
                  Ok(views.html.scanstats(sr, s, started))
                case BadStatsRequest => Ok("Please refresh the page later. We're busy.")
              }
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