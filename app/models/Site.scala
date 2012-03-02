package models
import org.openqa.selenium.WebDriver
import org.openqa.selenium.By

/**
 * Configuration for a site.
 */
abstract class Site {
  /**
   * Log in to a website using the given username and password
   */
  def login(driver: WebDriver, creds: Credentials): Unit

  def inSite(url: String): Boolean

}

/**
 *
 */
class CourseraSite(site: String) extends Site {
  def login(driver: WebDriver, creds: Credentials) {
    driver.get("https://www.coursera.org/" + site)
    driver.findElement(By.id("login_normal")).click()

    driver.findElement(By.name("email")).sendKeys(creds.username)
    driver.findElement(By.name("password")).sendKeys(creds.password)
    driver.findElement(By.name("password")).submit()

    driver.findElement(By.linkText("here")).click()
  }

  def inSite(url: String) = url.contains("www.coursera.org") && url.contains(site)
}
