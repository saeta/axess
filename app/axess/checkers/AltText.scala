package axess.checkers

import org.openqa.selenium.WebDriver
import org.openqa.selenium.By
import scala.collection.JavaConversions._

class AltText extends Checker {

  def checkPage(browser: WebDriver): List[String] = {
    for {
      elem <- browser.findElements(By.tagName("img")).toList
      if elem.getAttribute("alt") == null
    } yield "Missing alt text for image: " + elem.getAttribute("src")
  }

  def category = "Accessibility"

}