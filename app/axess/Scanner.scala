package axess

import akka.actor.Actor
import scala.collection.immutable.Stack
import akka.routing.RoundRobinRouter
import akka.actor.Props
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.openqa.selenium.WebDriver
import org.openqa.selenium.By
import models.CourseraSite
import models.Credentials
import models.SiteType
import scala.collection.JavaConversions._

class Scanner extends Actor {
  var browser: WebDriver = _
  var site: SiteType = _
  val checkers = List()

  override def preStart() {
    browser = new HtmlUnitDriver
    site = new CourseraSite("playspace")
    site.login(browser, Credentials(1, "REMOVED", "REMOVED", "NOTHING"))
  }

  def scanPage(url: String) = {
    browser.get(url)
    val f = browser.findElements(By.tagName("a")).map(
      _.getAttribute("href")).map(makeCanonical(url, _)).filter(site.inSite(_)).toSet
    sender ! NewUrls(f)
  }

  def processPage(url: String) = {

  }

  def makeCanonical(curPage: String, linkText: String): String = "TODO" // TODO

  def receive = {
    case ScanPage(url) => {
      scanPage(url)
      processPage(url)
    }

  }
}