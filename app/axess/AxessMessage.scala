package axess

import models.Site

// Messages to Play Frontend
case class StartScan(siteId: Long)
case class ScanStarted(scanId: Long)
case object AxessStatsQuery
case class AxessStats(queueLength: Int)

// Messages between Axess and Scan Manager Backends
case class ScanSite(scanId: Long, site: Site)
case class ScanComplete(scanId: Long)
case class ManagerBusy(s: ScanSite)
case class ScanError(s: ScanSite)
// TODO: ScanStats messages

// Messages between Scan Manager and worker backends
case class NewSite(site: Site) // Clears cookies, etc. Prepares workers for site
case class ScanPage(url: String)
case class PageNote(category: String, msg: String) // (URL provided by PageScanResult)
case class PageScanResult(url: String, notes: List[PageNote])
case class NewUrls(urls: Set[String])
