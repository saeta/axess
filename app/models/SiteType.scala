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

package models
import org.openqa.selenium.WebDriver
import org.openqa.selenium.By

/**
 * Configuration for a site.
 */
abstract class SiteType {
  var site: Site = _
  def configure(s: Site) = site = s

  /**
   * Log in to a website using the given username and password
   */
  def login(driver: WebDriver): Unit

  def inSite(url: String): Boolean

}

/**
 * Configuration for a Coursera-based Site.
 */
class CourseraSite extends SiteType {
  def login(driver: WebDriver) {
    driver.get(site.home)
    driver.findElement(By.partialLinkText("Login")).click()

    driver.findElement(By.name("email")).sendKeys(site.usr)
    driver.findElement(By.name("password")).sendKeys(site.pwd)
    driver.findElement(By.name("password")).submit()

    driver.findElement(By.linkText("here")).click()
  }

  def inSite(url: String) =
    url != null &&
      url.contains(site.home) &&
      !url.contains("lecture/download")
}

class BasicSite extends SiteType {
  def login(driver: WebDriver) {
    driver.get(site.home)
  }

  def inSite(url: String) = url != null && url.startsWith(site.home)
}

class WebAuthSite extends SiteType {
  def webauthLogin(driver: WebDriver) = {
    if (driver.getTitle().contains("WebLogin")) {
      driver.findElement(By.name("username")).sendKeys(site.usr)
      driver.findElement(By.name("password")).sendKeys(site.pwd)
      driver.findElement(By.name("password")).submit()
    } else {
      throw new RuntimeException(
        "We don't appear to be at a Stanford WebLogin page. Title: " +
          driver.getTitle())
    }
  }

  def login(driver: WebDriver) {
    driver.get(site.home)
    webauthLogin(driver)
  }

  def inSite(url: String) = url != null && url.startsWith(site.home)
}

class CourseWorkSite extends WebAuthSite {
  override def login(driver: WebDriver) {
    driver.get(site.home)
    driver.findElement(By.id("loginLink1")).click()
    webauthLogin(driver)
  }
}

class CourseWareSite extends WebAuthSite {

  override def login(driver: WebDriver) {
    driver.get(site.home)
    println(driver.getTitle())
    driver.findElement(By.id("shib-login")).submit()
    driver.findElement(By.linkText("this link")).click()
    println(driver.getPageSource())
    println(driver.getTitle())
    println(driver.getCurrentUrl())
    val form = driver.findElement(By.id("idpSelectInput"))
    form.sendKeys("Stanford University")
    form.submit()
    //    driver.findElement(By.partialLinkText("Stanford University")).click()
    println(driver.getTitle())
    webauthLogin(driver)
  }

  // TODO: exclude some of the download urls using the inSite function
}