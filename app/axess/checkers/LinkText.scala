package axess.checkers

import org.openqa.selenium.WebDriver
import org.openqa.selenium.By
import scala.collection.JavaConversions._

class LinkText extends Checker {

  val digits = "^\\W*\\d+\\W*$".r
  val here = "^here$".r
  val click_here = "^click here$".r

  def checkPage(browser: WebDriver): List[String] = {
    for {
      elem <- browser.findElements(By.tagName("a")).toList
      if elem.getAttribute("href") != null
      txt = elem.getText()
      if digits.findFirstIn(txt) != None ||
        here.findFirstIn(txt) != None ||
        click_here.findFirstIn(txt) != None
    } yield "Inaccessibile link text: \"" + txt + "\" (" + elem + ")"
  }

  def category = "Accessibility"

}
