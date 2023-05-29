package models

case class CancelBookingResult(success: Boolean, message: String = "", numberOfFreedTables: Int = 0, numberOfRemainingTables: Int = 0)
