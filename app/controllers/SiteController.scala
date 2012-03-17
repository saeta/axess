package controllers

import play.api.mvc._
import play.api._
import play.api.data._
import play.api.data.Forms._
import models._

object SiteController extends Controller {

  val newSiteForm = Form(mapping(
    "tag" -> nonEmptyText,
    "usr" -> nonEmptyText,
    "pwd" -> nonEmptyText,
    "home" -> nonEmptyText,
    "type" -> nonEmptyText,
    "dsc" -> text)(NewSite.apply)(NewSite.unapply))

  def all = Action {
    Ok(views.html.sites(Site.all()))
  }
  def delete(id: Long) = Action {
    Site.delete(id)
    Redirect(routes.SiteController.all)
  }

  def newSitePost = Action { implicit request =>
    newSiteForm.bindFromRequest.fold(
      errors => {
        BadRequest(views.html.sitesNew(errors))
      },
      good => {
        Site.create(good)
        Redirect(routes.SiteController.all)
      })
  }

  def newSiteGet = Action { implicit request =>
    Ok(views.html.sitesNew(newSiteForm))
  }
}