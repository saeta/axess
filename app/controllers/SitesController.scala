package controllers

import play.api.mvc._
import play.api._

object SitesController extends Controller {
  def all = Action { Ok("Hello world") }
}