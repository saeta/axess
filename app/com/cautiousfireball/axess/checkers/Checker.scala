package com.cautiousfireball.axess.checkers
import org.openqa.selenium.WebDriver

trait Checker {
  def checkPage(browser: WebDriver)

  def category: String
}
