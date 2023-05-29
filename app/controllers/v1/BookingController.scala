package controllers.v1

import models.{BookingTablesPayload, CancelBookingPayload, InitializedRestaurantPayload}
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

  def initRestaurant() = Action(parse.json) { implicit request =>
    request.body.validate[InitializedRestaurantPayload] match {
      case JsSuccess(request, _) => {
        val result = bookingService.initializeRestaurant(request.number_of_tables)
        Ok(Json.toJson(result))
      }
      case e: JsError => BadRequest
    }
  }

  def reserveTables() = Action(parse.json) { implicit request =>
    request.body.validate[BookingTablesPayload] match {
      case JsSuccess(request, _) => {
        val result = bookingService.bookTables(request.number_of_people)
        Ok(Json.toJson(result))
      }
      case e: JsError => BadRequest
    }
  }

  def cancelBooking() = Action(parse.json) { implicit request =>
    request.body.validate[CancelBookingPayload] match {
      case JsSuccess(request, _) => {
        val result = bookingService.cancelBooking(request.booking_id)
        Ok(Json.toJson(result))
      }
      case e: JsError =>
        BadRequest
    }
  }

  def getStatus = Action {  implicit request: Request[AnyContent] =>
    val allBookings = bookingService.getAllBooking
    val allTables = bookingService.getAllTables
    Ok(Json.obj("tables" -> allTables, "bookings" -> allBookings))
  }
}
