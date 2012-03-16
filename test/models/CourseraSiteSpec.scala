package models

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import org.openqa.selenium._
import org.openqa.selenium.htmlunit.HtmlUnitDriver

class CourseraSiteSpec extends Specification {

  "CourseraSite" should {
    "login successfully to playspace" in {

      val password: Option[String] = None
      password match {
        case None =>
          skipped("no password")
        case Some(pwd) =>
          val browser = new HtmlUnitDriver
          val site = new CourseraSite
          site.configure(
            Site(1,
              "playspace",
              "testuser@cs.stanford.edu",
              pwd,
              "https://www.coursera.org/playspace",
              Some("axess.CourseraSite"),
              None))

          site.login(browser)
          browser.getCurrentUrl() must startWith("https://www.coursera.org/playspace/class")
      }
    }
    "login successfully to saas" in {

      val password: Option[String] = None
      password match {
        case None =>
          skipped("no password")
        case Some(pwd) =>
          val browser = new HtmlUnitDriver
          val site = new CourseraSite
          site.configure(
            Site(1,
              "saas",
              "testuser@cs.stanford.edu",
              pwd,
              "https://www.coursera.org/saas",
              Some("axess.CourseraSite"),
              None))

          site.login(browser)
          browser.getCurrentUrl() must startWith("https://www.coursera.org/saas/class")
      }
    }

    "be created by reflection" in {
      val name = "models.CourseraSite"
      val clazz = Class.forName(name)
      val cs = clazz.getConstructors()(0).newInstance()

      (clazz != null) === (true) and
        (clazz.getConstructors()(0).newInstance() != null) === (true)
    }
  }
}