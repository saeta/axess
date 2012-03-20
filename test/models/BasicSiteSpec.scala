package models

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import org.openqa.selenium._
import org.openqa.selenium.htmlunit.HtmlUnitDriver

class BasicSiteSpec extends Specification {

  "BasicSite" should {
    "get the Stanford homepage" in {
      val browser = new HtmlUnitDriver
      val site = new BasicSite
      site.configure(
        Site(1,
          "stanford-home",
          "", // username
          "", // password
          "http://www.stanford.edu/",
          "models.BasicSite",
          ""))

      site.login(browser)
      browser.getCurrentUrl() must startWith("http://www.stanford.edu/")
    }

    "get the Stanford homepage over SSL" in {
      val browser = new HtmlUnitDriver
      val site = new BasicSite
      site.configure(
        Site(1,
          "stanford-ssl",
          "",
          "",
          "https://www.stanford.edu/",
          "models.BasicSite",
          ""))

      site.login(browser)
      browser.getCurrentUrl() must startWith("https://www.stanford.edu/")
    }

    "be created by reflection" in {
      val name = "models.BasicSite"
      val clazz = Class.forName(name)
      val cs = clazz.getConstructors()(0).newInstance()

      (clazz != null) === (true) and
        (clazz.getConstructors()(0).newInstance() != null) === (true)
    }
  }
}
