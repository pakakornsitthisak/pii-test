package services

import com.google.inject.Inject
import models.{BookingResult, CancelBookingResult, InitResult}
import repositories.BookingRepository

import scala.concurrent.ExecutionContext

class BookingService @Inject() (bookingRepository: BookingRepository)(implicit ec: ExecutionContext) {

  final val NUMBER_OF_SEATS = 4

  def getAllTables = bookingRepository.getAllTables

  def getAllBooking = bookingRepository.getAllBookings

  def initializeRestaurant(numberOfTables: Int) = {
    if (bookingRepository.isInitialize) {
      InitResult(success = false, message = "Restaurant is already initialized.")
    } else {
      bookingRepository.clear()
      for (i <- 0 to numberOfTables - 1) {
        bookingRepository.insertTable(i)
      }
      InitResult(success = true)
    }
  }

  def bookTables(numberOfPeople: Int) = {
    if (bookingRepository.isInitialize) {
      val remainingTables = bookingRepository.getRemainingTables
      val neededTables = if (numberOfPeople % NUMBER_OF_SEATS == 0) numberOfPeople / NUMBER_OF_SEATS else numberOfPeople / NUMBER_OF_SEATS + 1
      if (neededTables <= remainingTables) {
        val nextTableIds = (0 to neededTables - 1).map(i => {
          val nextTableId = bookingRepository.getNextFreeTableId
          bookingRepository.updateTable(nextTableId, true)
          nextTableId
        })
        val bookingId = bookingRepository.insertBooking(nextTableIds.toList)
        val numberOfRemainingTables = bookingRepository.getRemainingTables
        BookingResult(success = true, bookingId = bookingId, numberOfBookedTables = neededTables, numberOfRemainingTables = numberOfRemainingTables)
      } else {
        BookingResult(success = false, message = "Not enough tables")
      }
    } else {
      BookingResult(success = false, message = "Restaurant is not initialized.")
    }
  }

  def cancelBooking(bookingId: Int) = {
    if (bookingRepository.isInitialize) {
      if (bookingRepository.hasBooking(bookingId)) {
        val booking = bookingRepository.getBooking(bookingId)
        bookingRepository.deleteBooking(booking)
        for (i <- 0 to booking.tables.length - 1) {
          bookingRepository.updateTable(booking.tables(i), false)
        }
        val numberOfRemainingTables = bookingRepository.getRemainingTables
        CancelBookingResult(success = true, numberOfFreedTables = booking.tables.length, numberOfRemainingTables = numberOfRemainingTables)
      } else {
        CancelBookingResult(success = false, message = "Booking not found")
      }
    } else {
      CancelBookingResult(success = false, message = "Restaurant is not initialized.")
    }

  }
}
