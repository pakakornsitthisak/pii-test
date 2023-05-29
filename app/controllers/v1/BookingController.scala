package controllers.v1

import play.api.libs.json.Json
import play.api.mvc._

import javax.inject._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class BookingController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def initRestaurant() = Action { implicit request: Request[AnyContent] =>
    Ok(Json.obj("posts" -> ""))
  }

  def reserveTables() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def cancelBooking() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }
}
