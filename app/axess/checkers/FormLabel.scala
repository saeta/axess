package axess.checkers

import org.openqa.selenium.WebDriver
import scala.collection.immutable.List
import org.openqa.selenium.WebElement
import org.openqa.selenium.By
import scala.collection.JavaConversions._

class FormLabel extends Checker {

  val prefix = "Form Markup Error: "
  val p = prefix

  def inputNames(form: WebElement) = {
    val names = scala.collection.mutable.Set[String]()
    val errors = scala.collection.mutable.Set[String]()
    for {
      elem <- form.findElements(By.tagName("input"))
      typ = elem.getAttribute("type")
      if typ != null && typ != "submit" && typ != "hidden"
      name = elem.getAttribute("name")
    } {
      name match {
        case null => errors += p + "Name was null in: " + elem
        case "" => errors += p + "Name was '' in: " + elem
        case n: String =>
          if (names.contains(n)) {
            errors += p + "Two input elements have the same name: " + elem
          } else {
            names += n
          }
      }
    }
    (names.toSet, errors.toSet)
  }

  def labels(form: WebElement) = {
    val names = scala.collection.mutable.Set[String]()
    val errors = scala.collection.mutable.Set[String]()
    for {
      elem <- form.findElements(By.tagName("label"))
      forName = elem.getAttribute("for")
    } {
      forName match {
        case null => errors += p + "for was null: " + elem
        case "" => errors += p + "for was '': " + elem
        case fn: String =>
          if (names.contains(fn)) {
            errors += p + "Two labels have the same name: " + elem
          } else {
            names += fn
          }
      }
    }
    (names.toSet, errors.toSet)
  }

  def checkPage(browser: WebDriver): List[String] = {
    for {
      form <- browser.findElements(By.tagName("form")).toList
      (inputNms, inputErs) = inputNames(form)
      (labelNms, labelErs) = labels(form)
      unlabeledInputs = inputNms -- labelNms
      extraLabels = labelNms -- inputNms
    } yield {
      List(inputErs,
        labelErs,
        unlabeledInputs.map(a => p + "Unlabeled input: " + a),
        extraLabels.map(a => p + "Extra label, no matching input: " + a)).flatten
    }
  }.flatten[String]

  def category(): String = "Accessibility"

}
