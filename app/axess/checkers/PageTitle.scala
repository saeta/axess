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

class PageTitle extends Checker {

  def checkPage(browser: WebDriver): List[String] = {
    if (browser.getTitle() == null || browser.getTitle() == "") {
      List("No page title for page: " + cutUrl(browser.getCurrentUrl()))
    } else {
      List()
    }
  }

  def cutUrl(url: String) = {
    if (url.length() > 60) {
      url.substring(12, 57) + "..."
    } else {
      url
    }
  }

  def category = "Accessibility"

}