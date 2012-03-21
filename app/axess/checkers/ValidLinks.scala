package axess.checkers

import org.openqa.selenium.WebDriver
import org.openqa.selenium.By
import scala.collection.JavaConversions._
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.client.methods.HttpGet

// Consider turning Checkers into Actors, that can dispatch work themselves?
class ValidLinks extends Checker {
  val client = new DefaultHttpClient

  def checkPage(browser: WebDriver): List[String] = {
    try {
      for {
        elem <- browser.findElements(By.tagName("a")).toList
        if elem.getAttribute("href") != null
        get = new HttpGet(elem.getAttribute("href"))
        response = client.execute(get)
        code = response.getStatusLine().getStatusCode()
        istrm = response.getEntity().getContent()
        a = {istrm.close(); true}
        if code >= 400
      } yield "Broken Link: " + elem
    } catch {
      case e => List()
    }
  }

  def category = "Usability"

}
