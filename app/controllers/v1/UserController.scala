package controllers.v1

import models.BookingTablesPayload
import play.api.libs.json.Json
import play.api.mvc._
import services.BookingService
import play.api.libs.json._

import javax.inject._
import serializers.v1.ObjectDeserializer._
import serializers.v1.ObjectSerializer._


/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class BookingController @Inject()(bookingService: BookingService, val controllerComponents: ControllerComponents) extends BaseController {

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }
}
