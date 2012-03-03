package controllers

import play.api.mvc._
import play.api._
import play.api.data._
import play.api.data.Forms._
import models._

object ScanController extends Controller {

  def start(id: Long) = Action {
    Ok("Starting scan...")
  }

  def status(id: Long) = Action {
    Ok("A scan status...")
  }

  def results(id: Long) = Action {
    Ok("Scan results!")
  }

}