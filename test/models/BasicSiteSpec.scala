// Copyright 2012 Brennan Saeta
//
// This file is part of Axess
//
// Axess is free software: you can redistribute it and/or modify
// it under the terms of the GNU Affero General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// Axess is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Affero General Public License for more details.
//
// You should have received a copy of the GNU Affero General Public License
// along with Axess.  If not, see <http://www.gnu.org/licenses/>.

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
