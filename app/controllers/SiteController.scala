package controllers

import play.api.mvc._
import play.api._
import play.api.data._
import play.api.data.Forms._
import models._

object SiteController extends Controller {

  val newSiteForm = Form(tuple(
    "tag" -> nonEmptyText,
    "usr" -> nonEmptyText,
    "pwd" -> nonEmptyText,
    "home" -> nonEmptyText))

  val editSiteForm = Form(mapping(
    "id" -> longNumber,
    "tag" -> nonEmptyText,
    "usr" -> nonEmptyText,
    "pwd" -> nonEmptyText,
    "home" -> nonEmptyText,
    "stype" -> optional(text),
    "dsc" -> optional(text))(Site.apply)(Site.unapply))

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
        Site.create(good._1, good._2, good._3, good._4)
        Redirect(routes.SiteController.all)
      })
  }

  def newSiteGet = Action { implicit request =>
    Ok(views.html.sitesNew(newSiteForm))
  }

  def edit(id: Long) = Action {
    val site = Site.getSite(id)
    site match {
      case Some(s) => Ok(views.html.siteEdit(id, s.tag, editSiteForm.fill(s)))
      case None => NotFound
    }
  }

  def save(id: Long) = Action { implicit request =>
    val site = Site.getSite(id)
    site match {
      case Some(s) => {
        editSiteForm.bindFromRequest.fold(
          errors => {
            BadRequest(views.html.siteEdit(id, s.tag, errors))
          },
          good => {
            Site.update(good)
            Redirect(routes.SiteController.all)
          })
      }
      case None => NotFound
    }
  }
}