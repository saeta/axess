package axess

import models.Site
import akka.actor.ActorRef

// Messages to Play Frontend
case class StartScan(siteId: Long)
case class ScanStarted(scanId: Long)
case object AxessStatsQuery
case class AxessStats(queueLength: Int) {
  def mkString = "queue length: %d".format(queueLength)
}
case class StatsRequest(scanId: Long)
case class StatsResponse(numSeen: Int, numDone: Int, as: AxessStats) {
  def mkString =
    "Stats Response: seen: %d, done: %s, AS: %s".format(
      numSeen, numDone, as.mkString)
}
case object BadStatsRequest

// Messages between Axess and Scan Manager Backends
case class ScanSite(scanId: Long, site: Site)
case class ScanComplete(scanId: Long)
case class ManagerBusy(s: ScanSite)
case class ScanError(s: ScanSite)
case class ScanStatsRequest(respondTo: ActorRef)
case class ScanStatsResult(numSeen: Int, numDone: Int, respondTo: ActorRef)

// Messages between Scan Manager and worker backends
case class NewSite(site: Site) // Clears cookies, etc. Prepares workers for site
case class ScanPage(url: String)
case class PageNote(category: String, msg: String) // (URL provided by PageScanResult)
case class PageScanResult(url: String, notes: List[PageNote])
case class NewUrls(urls: Set[String])
