package controllers

import play.api.mvc._
import play.api._
import play.api.data._
import play.api.data.Forms._
import models._

object SiteController extends Controller {

  val newSiteForm = Form(tuple(
    "tag" -> nonEmptyText,
    "username" -> nonEmptyText,
    "password" -> nonEmptyText,
    "home" -> nonEmptyText))

  def all = Action {
    Ok(views.html.sites(Site.all()))
  }
  def delete(id: Long) = Action {
    Site.delete(id)
    Redirect(routes.SiteController.all)
  }

  // TODO: create edit and modify forms!

  def newSitePost = Action { implicit request =>
    newSiteForm.bindFromRequest.fold(
      errors => {
        BadRequest(views.html.sitesNew(errors))
      },
      good => {
        Site.create(good._1, good._2, good._3, good._4)
        Redirect(routes.SiteController.all)
      })
  }

  def newSiteGet = Action { implicit request =>
    Ok(views.html.sitesNew(newSiteForm))
  }
}