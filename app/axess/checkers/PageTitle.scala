package axess.checkers
import org.openqa.selenium.WebDriver

class PageTitle extends Checker {

  def checkPage(browser: WebDriver): List[String] = {
    if (browser.getTitle() == null || browser.getTitle() == "") {
      List("No page title for page: " + cutUrl(browser.getCurrentUrl()))
    } else {
      List()
    }
  }

  def cutUrl(url: String) = {
    if (url.length() > 60) {
      url.substring(12, 57) + "..."
    } else {
      url
    }
  }

  def category = "Accessibility"

}