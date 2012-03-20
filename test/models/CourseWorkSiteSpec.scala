package models

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import org.openqa.selenium._
import org.openqa.selenium.htmlunit.HtmlUnitDriver

class CourseWorkSiteSpec extends Specification {

  "CourseWork site" should {
    "login to https://coursework.stanford.edu/" in {
      val password: Option[String] = None

      password match {
        case None => skipped("no password")
        case Some(pwd) =>
          val browser = new HtmlUnitDriver
          val site = new CourseWorkSite
          site.configure(Site(1,
            "coursework",
            "saeta@stanford.edu",
            pwd,
            "https://coursework.stanford.edu",
            "models.CourseWorkSite",
            ""))

          site.login(browser)
          browser.getCurrentUrl() === ("https://coursework.stanford.edu/portal/")
      }
    }

    "be created by reflection" in {
      val name = "models.CourseWorkSite"
      val clazz = Class.forName(name)
      (clazz != null) === true and
        (clazz.getConstructors()(0).newInstance() != null) === true
    }
  }
}