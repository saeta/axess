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