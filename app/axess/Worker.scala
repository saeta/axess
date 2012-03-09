package axess

import akka.actor.Actor
import scala.collection.immutable.Stack
import akka.routing.RoundRobinRouter
import akka.actor.Props
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.openqa.selenium.WebDriver
import org.openqa.selenium.By
import models.CourseraSite
import models.SiteType
import scala.collection.JavaConversions._
import models.Site
import axess.checkers._

class Worker extends Actor {
  var browser: WebDriver = new HtmlUnitDriver
  var siteType: SiteType = _
  val checkers = List(new AltText())

  def scanPage(url: String) = {
    browser.get(url)
    val f = browser.findElements(By.tagName("a")).map(
      _.getAttribute("href")).map(makeCanonical(url, _)).filter(siteType.inSite(_)).toSet
    sender ! NewUrls(f)
  }

  def processPage(url: String) = {
    val list = for {
      chk <- checkers
      msgs <- chk.checkPage(browser)
    } yield {
      PageNote(chk.category, msgs)
    }
    sender ! PageScanResult(url, list)
  }

  def login(site: Site) = {
    browser.manage().deleteAllCookies()
    val s = Class.forName(site.stype.get).newInstance().asInstanceOf[SiteType]
    s.configure(site)
    siteType = s
    siteType.login(browser)
  }

  def makeCanonical(curPage: String, linkText: String): String = "TODO" // TODO

  def receive = {
    case ScanPage(url) =>
      scanPage(url)
      processPage(url)
    case NewSite(site) =>
      login(site)
  }
}