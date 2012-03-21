// Copyright 2012 Brennan Saeta
//
// This file is part of Axess
//
// Axess is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// Axess is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with Axess.  If not, see <http://www.gnu.org/licenses/>.

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
              "models.CourseraSite",
              ""))

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
              "models.CourseraSite",
              ""))

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