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
 *
 */
class CourseraSite extends SiteType {
  def login(driver: WebDriver) {
    driver.get(site.home)
    driver.findElement(By.linkText("Coursera Login")).click()

    driver.findElement(By.name("email")).sendKeys(site.usr)
    driver.findElement(By.name("password")).sendKeys(site.pwd)
    driver.findElement(By.name("password")).submit()

    driver.findElement(By.linkText("here")).click()
  }

  def inSite(url: String) = url != null && url.contains(site.home)
}
