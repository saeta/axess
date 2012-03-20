package controllers

import play.api.mvc._
import models.DashboardEntry

object Application extends Controller {

  def index = Action {
    Ok(views.html.index(DashboardEntry.get()))
  }
  def successes = Action {
    Ok(views.html.successes())
  }
}