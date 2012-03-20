package models

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import org.openqa.selenium._
import org.openqa.selenium.htmlunit.HtmlUnitDriver

class CourseWareSiteSpec extends Specification {

  "CourseWare site" should {
    "login to https://courseware.stanford.edu/" in {
      val password: Option[String] = None

      password match {
        case None =>
          //          skipped("no password")
          skipped("this was completely broken on courseware's end.")
        case Some(pwd) =>
          val browser = new HtmlUnitDriver
          val site = new CourseWareSite
          site.configure(Site(1,
            "courseware",
            "saeta@stanford.edu",
            pwd,
            "http://courseware.stanford.edu",
            "models.CourseWareSite",
            ""))

          site.login(browser)
          browser.getCurrentUrl() === ("https://courseware.stanford.edu/pg/courses/mine")
      }
    }

    "be created by reflection" in {
      val name = "models.CourseWareSite"
      val clazz = Class.forName(name)
      (clazz != null) === true and
        (clazz.getConstructors()(0).newInstance() != null) === true
    }
  }

}