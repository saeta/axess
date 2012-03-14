package axess

import akka.actor.Actor
import scala.collection.immutable.Stack
import akka.routing.RoundRobinRouter
import akka.actor.Props
import models._
import akka.actor.ActorRef
import akka.actor.OneForOneStrategy
import akka.util.duration._
import akka.actor.SupervisorStrategy._
import akka.event.Logging
import axess._

class Axess extends Actor {
  val log = Logging(context.system, this)

  val NUM_MANAGERS = 2

  val scanQueue = scala.collection.mutable.Queue[Long]()
  val managers = scala.collection.mutable.Set[ActorRef]()
  val scanMap = scala.collection.mutable.Map[Long, ActorRef]()
  val freeSet = scala.collection.mutable.Set[ActorRef]()

  override val supervisorStrategy = OneForOneStrategy(maxNrOfRetries = 3,
    withinTimeRange = 5 seconds) {
      case _: Exception => Restart
    }

  override def preStart = {
    val managerSeq = for (i <- 0 until NUM_MANAGERS)
      yield context.actorOf(Props[ScanManager], "manager-%d".format(i))
    managers ++= managerSeq
    freeSet ++= managerSeq
    val numErroredOut = Scan.errorAllUnterminatedScans()
    if (numErroredOut > 0) {
      log.warning("{} Scans errored out on boot", numErroredOut)
    }
  }

  /**
   * Reply with stats on how things are going.
   */
  def stats = {
    AxessStats(scanQueue.length, freeSet.size, scanMap.size)
  }
  /**
   * Gets a site given a scanId
   */
  def siteForScan(scanId: Long) = Scan.getScan(scanId) match {
    case Some(scan) => Site.getSite(scan.siteId)
    case None => None
  }

  /**
   * Dispatches new scans if available.
   */
  // TODO: put in a loop that keeps adding as long as there are managers available
  def startScanFromQueue(): Unit = {
    // There must be (1) an available worker, and (2) something in the queue
    if (scanMap.size < NUM_MANAGERS && scanQueue.size > 0) {
      // Find a manager
      val manager = freeSet.head
      // Find a site we need to scan. (In a loop to handle option edge cases)
      var notFound = true
      while (notFound) {
        val scanId = scanQueue.dequeue()
        val siteOpt = siteForScan(scanId)
        siteOpt match {
          case None => true // Do nothing
          case Some(site) => {
            notFound = false
            scanMap(scanId) = manager
            freeSet -= manager
            manager ! ScanSite(scanId, site)
          }
        }
      }
    }
  }

  def completeScan(id: Long): Unit = {
    if (scanMap.contains(id)) {
      val actorRef = scanMap.remove(id)
      actorRef match {
        case None =>
        case Some(ar) =>
          freeSet += ar
      }
    }
    startScanFromQueue()
  }

  def DEBUG_MANAGER_BUSY(mg: ManagerBusy) = {
    val scanId = mg.scanSite.scanId
    val scanManager = scanMap(scanId)
    println("Manager BUSY!")
    log.error("Worker busy error! SentTo: {},  Scan: {}, In Free Set: {}",
      scanManager, scanId, freeSet.contains(scanManager))
  }

  def DEBUG_SCAN_ERROR(se: ScanError) = {
    log.error("SCAN ERROR! {}", se)
    Scan.error(se.s.scanId, "Scan Error. Cause Unknown")
  }

  def receive() = {
    case StartScan(siteId) => {
      val scanId = Scan.newScan(siteId)
      sender ! ScanStarted(scanId)
      scanQueue.enqueue(scanId)
      startScanFromQueue()
    }
    case AxessStatsQuery =>
      sender ! stats
    case mb: ManagerBusy => DEBUG_MANAGER_BUSY(mb)
    case se: ScanError => DEBUG_SCAN_ERROR(se)
    case ScanComplete(scanId) =>
      completeScan(scanId)
    case StatsRequest(scanId) =>
      if (scanMap.contains(scanId)) {
        scanMap(scanId) ! ScanStatsRequest(sender)
      } else {
        sender ! BadStatsRequest
      }
    case ScanStatsResult(seen, done, resp) =>
      resp ! StatsResponse(seen, done, stats)
  }

}