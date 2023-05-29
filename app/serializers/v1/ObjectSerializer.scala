package serializers.v1

import models.{Booking, BookingResult, CancelBookingResult, InitResult}
import play.api.libs.json.{JsObject, Json, Writes}

object ObjectSerializer {

  implicit val bookingResultJsonWrites = new Writes[BookingResult] {
    def writes(result: BookingResult): JsObject = Json.obj(
      "success" -> result.success,
      "message" -> result.message,
      "booking_id" -> result.bookingId,
      "number_of_booked_tables" -> result.numberOfBookedTables,
      "number_of_remaining_tables" -> result.numberOfRemainingTables,
    )
  }

  implicit val cancelNookingResultJsonWrites = new Writes[CancelBookingResult] {
    def writes(result: CancelBookingResult): JsObject = Json.obj(
      "success" -> result.success,
      "message" -> result.message,
      "number_of_freed_tables" -> result.numberOfFreedTables,
      "number_of_remaining_tables" -> result.numberOfRemainingTables,
    )
  }

  implicit val initResultJsonWrites = new Writes[InitResult] {
    def writes(result: InitResult): JsObject = Json.obj(
      "success" -> result.success,
      "message" -> result.message,
    )
  }

  implicit val bookingJsonWrites = new Writes[Booking] {
    def writes(result: Booking): JsObject = Json.obj(
      "id" -> result.id,
      "tables" -> result.tables,
    )
  }

}
