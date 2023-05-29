package models

case class BookingResult(success: Boolean, message: String = "", bookingId: Int = 0, numberOfBookedTables: Int = 0, numberOfRemainingTables: Int = 0)
