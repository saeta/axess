package axess.checkers
import org.openqa.selenium.WebDriver

trait Checker {
  def checkPage(browser: WebDriver): List[String]

  def category: String
}
