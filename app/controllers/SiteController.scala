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
        BadRequest(views.html.sitesNew(errors, Site.siteTypes()))
      },
      good => {
        Site.create(good)
        Redirect(routes.SiteController.all)
      })
  }

  def newSiteGet = Action { implicit request =>
    Ok(views.html.sitesNew(newSiteForm, Site.siteTypes()))
  }
}