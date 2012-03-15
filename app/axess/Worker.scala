package axess

import akka.actor.Actor
import scala.collection.immutable.Stack
import akka.routing.RoundRobinRouter
import akka.actor.Props
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
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
    if (siteType == null) throw new RuntimeException("Help!")
    val f = browser.findElements(By.tagName("a")).toSet.map {
      e: WebElement => e.getAttribute("href")
    }.map(
      s => makeCanonical(url, s)).filter(
        s => siteType.inSite(s))
    sender ! NewUrls(f)
  }

  def processPage(url: String) = {
    val list = for {
      chk <- checkers
      msgs <- chk.checkPage(browser)
    } yield {
      PageNote(chk.category, msgs)
    }
    sender ! PageScanResult(url, browser.getTitle(), list)
  }

  def login(site: Site) = {
    browser.manage().deleteAllCookies()
    //    val s = Class.forName(site.stype.get).newInstance().asInstanceOf[SiteType]
    val s = new CourseraSite()
    s.configure(site)
    siteType = s
    siteType.login(browser)
  }

  // TODO: make it work on relative links!
  def makeCanonical(curPage: String, linkText: String): String = {
    if (linkText == null) null
    else if (linkText.contains("#")) {
      linkText.split("#")(0)
    } else {
      linkText
    }
    //    if (linkText.contains("#")) {
    //      linkText.split("#")(0)
    //    } else {
    //      linkText
    //    }
  }

  def receive = {
    case ScanPage(url) =>
      scanPage(url)
      processPage(url)
    case NewSite(site) =>
      login(site)
  }
}