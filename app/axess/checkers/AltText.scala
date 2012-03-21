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

package axess.checkers

import org.openqa.selenium.WebDriver
import org.openqa.selenium.By
import scala.collection.JavaConversions._

class AltText extends Checker {

  def checkPage(browser: WebDriver): List[String] = {
    for {
      elem <- browser.findElements(By.tagName("img")).toList
      if elem.getAttribute("alt") == null
    } yield "Missing alt text for image: " + elem.getAttribute("src")
  }

  def category = "Accessibility"

}