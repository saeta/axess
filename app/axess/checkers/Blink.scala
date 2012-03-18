package axess.checkers

import org.openqa.selenium.WebDriver
import org.openqa.selenium.By
import scala.collection.JavaConversions._

class Blink extends Checker {

  def checkPage(browser: WebDriver): List[String] = {
    for {
      elem <- browser.findElements(By.tagName("blink")).toList
    } yield "Blink element: " + elem
  }

  def category(): String = "Accessibility"

}