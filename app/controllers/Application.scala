package controllers

import play.api._
import play.api.mvc._
import play.api.libs.concurrent._
import akka.actor.ActorSystem
import akka.actor.Actor
import akka.actor.Props
import akka.pattern._
import akka.util.duration._
import akka.util.Timeout
import axess._
import play.api.Play._
import models.DashboardEntry

object Application extends Controller {

  def index = Action {
    Ok(views.html.index(DashboardEntry.get()))
  }
}