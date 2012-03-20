package views

object NavbarHeaders {
  val defaults: List[(play.api.mvc.Call, String)] = List(
    (controllers.routes.SiteController.all(), "Sites"),
    (controllers.routes.ScanController.summary(), "Scan Results"),
    (controllers.routes.Application.successes(), "Successes"),
    (controllers.routes.ScanController.sysstatus(), "Status")
  )
}