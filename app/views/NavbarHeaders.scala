// Copyright 2012 Brennan Saeta
//
// This file is part of Axess
//
// Axess is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// Axess is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with Axess.  If not, see <http://www.gnu.org/licenses/>.

package views

object NavbarHeaders {
  val defaults: List[(play.api.mvc.Call, String)] = List(
    (controllers.routes.SiteController.all(), "Sites"),
    (controllers.routes.ScanController.summary(), "Scan Results"),
    (controllers.routes.Application.successes(), "Successes"),
    (controllers.routes.ScanController.sysstatus(), "Status"))
}