// Copyright 2012 Brennan Saeta
//
// This file is part of Axess
//
// Axess is free software: you can redistribute it and/or modify
// it under the terms of the GNU Affero General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// Axess is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Affero General Public License for more details.
//
// You should have received a copy of the GNU Affero General Public License
// along with Axess.  If not, see <http://www.gnu.org/licenses/>.

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
  val checkers = List(new AltText(),
    new FormLabel(),
    new PageTitle(),
    new Blink(),
    new LinkText() /*,
    new ValidLinks()*/ ) // ValidLinks disabled due to performance concerns

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
    val s = Class.forName(site.stype).newInstance().asInstanceOf[SiteType]
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
  }

  def receive = {
    case ScanPage(url) =>
      scanPage(url)
      processPage(url)
    case NewSite(site) =>
      login(site)
  }
}