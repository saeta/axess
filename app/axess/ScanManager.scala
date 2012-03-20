package axess

import akka.actor._
import akka.actor.SupervisorStrategy._
import akka.pattern._
import akka.util.duration._
import akka.util.Timeout
import akka.routing.RoundRobinRouter
import akka.routing.Broadcast
import models.ScanMsg
import models.Scan
import akka.event.Logging

class ScanManager extends Actor {
  val log = Logging(context.system, this)
  val NUM_WORKERS = 10

  // Escalate everything up to axess which will take care of any issues by
  // restarting this whole ScanManager tree
  val escalator = AllForOneStrategy() {
    case _: Exception => Escalate
  }
  val workers = context.actorOf(Props[Worker].withRouter(
    RoundRobinRouter(NUM_WORKERS, supervisorStrategy = escalator)))

  override def supervisorStrategy = escalator

  override def postStop = curScan match {
    case None => true
    case Some(site) =>
      context.parent ! ScanError(site)
      log.error("Error: I'm shutting down!")
      Scan.error(site.scanId, "Unknown error. Stopped while scanning.")
  }

  /** Current site we're scanning. */
  var curScan: Option[ScanSite] = None
  /** Pages we have seen, but not yet scanned. */
  val pagesEncountered = scala.collection.mutable.Set[String]()
  /** Pages we have already scanned. */
  val pagesScanned = scala.collection.mutable.Set[String]()

  def sendDone() = curScan match {
    case None => throw new RuntimeException("oops")
    case Some(scan) => {
      if (pagesEncountered.size == pagesScanned.size && pagesScanned.size > 0) {
        Scan.finish(scan.scanId)
        updateStatsDb()
        curScan = None
        pagesEncountered.clear() // Free up memory
        pagesScanned.clear()
        context.parent ! ScanComplete(scan.scanId)
      }
    }
  }

  def updateStatsDb(): Unit = curScan match {
    case None =>
      log.error("UpdateStatsDB called when curScan was None!")
    case Some(scan) =>
      Scan.updateScanStats(scan.scanId, pagesEncountered.size, pagesScanned.size)
  }

  def scanSite(s: ScanSite) = curScan match {
    case Some(_) => sender ! ManagerBusy(s)
    case None => // normal
      // Set up internal state
      curScan = Some(s)
      pagesEncountered.clear()
      pagesScanned.clear()
      Scan.start(s.scanId)
      // Set up child state
      workers ! Broadcast(NewSite(s.site)) // Clear cookies
      // Start
      pagesEncountered += s.site.home
      workers ! ScanPage(s.site.home) // Start the process

  }

  def pageResult(r: PageScanResult) = curScan match {
    case None =>
      log.error("Dropping PageScanResults: {}", r)
    case Some(site) =>
      pagesScanned += r.url
      for (pageNote <- r.notes) {
        ScanMsg.addMsg(site.scanId,
          pageNote.category,
          if (r.title == null || r.title == "")
            "<ERROR: no page title!>"
          else
            r.title,
          r.url,
          pageNote.msg)
      }
      sendDone()
  }

  def newUrls(n: NewUrls) = curScan match {
    case None =>
      log.error("Dropped new URLs: {}", n)
    case Some(site) =>
      for {
        url <- n.urls
        if !pagesEncountered.contains(url)
      } {
        pagesEncountered += url
        ScanMsg.foundPage(site.scanId, url)
        workers ! ScanPage(url)
      }
      sendDone()
  }

  def receive = {
    case s: ScanSite => scanSite(s)
    case r: PageScanResult => pageResult(r)
    case n: NewUrls => newUrls(n)
    case ScanStatsRequest(resp) =>
      sender ! ScanStatsResult(pagesEncountered.size, pagesScanned.size, resp)
  }
}
