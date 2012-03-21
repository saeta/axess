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

package axess

import models.Site
import akka.actor.ActorRef

// Messages to Play Frontend
case class StartScan(siteId: Long)
case class ScanStarted(scanId: Long)
case object AxessStatsQuery
case class AxessStats(queueLength: Int, freeSetSize: Int, concurrentScans: Int)
case class StatsRequest(scanId: Long)
case class StatsResponse(numSeen: Int, numDone: Int, as: AxessStats) {
  def percentDone = ((numDone.toDouble / numSeen.toDouble) * 10).toInt * 10
}
case object BadStatsRequest

// Messages between Axess and Scan Manager Backends
case class ScanSite(scanId: Long, site: Site)
case class ScanComplete(scanId: Long)
case class ManagerBusy(scanSite: ScanSite)
case class ScanError(s: ScanSite)
case class ScanStatsRequest(respondTo: ActorRef)
case class ScanStatsResult(numSeen: Int, numDone: Int, respondTo: ActorRef)

// Messages between Scan Manager and worker backends
case class NewSite(site: Site) // Clears cookies, etc. Prepares workers for site
case class ScanPage(url: String)
case class PageNote(category: String, msg: String) // (URL provided by PageScanResult)
case class PageScanResult(url: String, title: String, notes: List[PageNote])
case class NewUrls(urls: Set[String])
