package serializers.v1

import models.{BookingTablesPayload, CancelBookingPayload, InitializedRestaurantPayload}
import play.api.libs.json.{JsPath, Reads}

object ObjectDeserializer {

  implicit val initializedRestaurantPayloadReads: Reads[InitializedRestaurantPayload] =
    (JsPath \ "number_of_tables").read[Int].map(InitializedRestaurantPayload.apply)

  implicit val bookingTablesPayloadReads: Reads[BookingTablesPayload] =
    (JsPath \ "number_of_people").read[Int].map(BookingTablesPayload.apply)

  implicit val cancelBookingTablesPayloadReads: Reads[CancelBookingPayload] =
    (JsPath \ "booking_id").read[Int].map(CancelBookingPayload.apply)
}
