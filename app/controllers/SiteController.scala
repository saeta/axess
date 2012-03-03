package controllers

import play.api.mvc._
import play.api._
import play.api.data._
import play.api.data.Forms._
import models._

object SiteController extends Controller {

  val newSiteForm = Form(tuple(
    "username" -> nonEmptyText,
    "password" -> nonEmptyText,
    "home" -> nonEmptyText,
    "dsc" -> text))

  def all = Action {
    Ok(views.html.sites(Site.all()))
  }
  def delete(id: Long) = Action {
    Site.delete(id)
    Redirect(routes.SiteController.all)
  }

}