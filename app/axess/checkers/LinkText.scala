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
