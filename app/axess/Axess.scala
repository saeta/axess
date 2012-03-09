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
import axess._

class Axess extends Actor {
  val NUM_MANAGERS = 5

  val scanQueue = new scala.collection.mutable.Queue[Long]()
  val managers = scala.collection.mutable.Set[ActorRef]()
  val scanMap = new scala.collection.mutable.HashMap[Long, ActorRef]()

  override val supervisorStrategy = OneForOneStrategy(maxNrOfRetries = 3,
    withinTimeRange = 5 seconds) {
      case _: Exception => Restart
    }

  override def preStart = {
    val managerSeq = for (i <- 0 until NUM_MANAGERS)
      yield context.actorOf(Props[ScanManager], "manager-%d".format(i))
    managers ++= managerSeq
  }

  override def postStop = {
    // TODO: error out all the current in progress scans
  }

  /**
   * Reply with stats on how things are going.
   */
  def stats = {
    AxessStats(scanQueue.length)
  }

  /** Busy Managers */
  def busy = Set() ++ scanMap.values
  /** Not busy managers */
  def notBusy = (Set() ++ managers) -- busy

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
      val manager = notBusy.head
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
            manager ! ScanSite(scanId, site)
          }
        }
      }
    }
  }

  def completeScan(id: Long): Unit = {
    if (scanMap.contains(id)) {
      scanMap.remove(id)
    }
    startScanFromQueue()
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
    case ManagerBusy(_) => throw new RuntimeException("Worker busy! Oops!") // TODO
    case ScanError(_) => throw new RuntimeException("Scan error!") // TODO
    case ScanComplete(scanId) =>
      completeScan(scanId)
  }

}