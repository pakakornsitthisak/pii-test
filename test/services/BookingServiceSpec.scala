package services
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play._
import org.mockito.Mockito._
import repositories.BookingRepository

import scala.collection.mutable.{ListBuffer => mutableList, Map => mutableMap}
import models.{Booking, BookingResult, CancelBookingResult, InitResult}
import org.mockito.Matchers.any
class BookingServiceSpec extends PlaySpec with MockitoSugar {

  "getAllTables" should {
    "return all tables from booking repo" in {
      val mockBookingRepository = mock[BookingRepository]
      val bookingService = new BookingService(mockBookingRepository)
      when(mockBookingRepository.getAllTables).thenReturn(mutableMap(1 -> false))
      bookingService.getAllTables mustBe mutableMap(1 -> false)
    }
  }

  "getAllBooking" should {
    "return all bookings from booking repo" in {
      val mockBookingRepository = mock[BookingRepository]
      val bookingService = new BookingService(mockBookingRepository)
      when(mockBookingRepository.getAllBookings).thenReturn(mutableList(Booking(1, List(1,2))))
      bookingService.getAllBooking mustBe mutableList(Booking(1, List(1,2)))
    }
  }

  "initializeRestaurant" should {
    "return false if restaurant is already initialized." in {
      val mockBookingRepository = mock[BookingRepository]
      val bookingService = new BookingService(mockBookingRepository)
      when(mockBookingRepository.isInitialize).thenReturn(true)
      bookingService.initializeRestaurant(5) mustBe InitResult(success = false, message = "Restaurant is already initialized.")
    }

    "return true if restaurant is initialized successfully." in {
      val mockBookingRepository = mock[BookingRepository]
      val bookingService = new BookingService(mockBookingRepository)
      when(mockBookingRepository.isInitialize).thenReturn(false)
      doNothing().when(mockBookingRepository).clear()
      doNothing().when(mockBookingRepository).insertTable(any())
      bookingService.initializeRestaurant(5) mustBe InitResult(success = true, message = "")
    }
  }

  "bookTables" should {
    "return false if restaurant is not already initialized." in {
      val mockBookingRepository = mock[BookingRepository]
      val bookingService = new BookingService(mockBookingRepository)
      when(mockBookingRepository.isInitialize).thenReturn(false)
      bookingService.bookTables(5) mustBe BookingResult(success = false, message = "Restaurant is not initialized.")
    }

    "return false if restaurant has not enough tables." in {
      val mockBookingRepository = mock[BookingRepository]
      val bookingService = new BookingService(mockBookingRepository)
      when(mockBookingRepository.isInitialize).thenReturn(true)
      when(mockBookingRepository.getRemainingTables).thenReturn(1)
      bookingService.bookTables(5) mustBe BookingResult(success = false, message = "Not enough tables")
    }

    "return true if restaurant has booked successfully." in {
      val mockBookingRepository = mock[BookingRepository]
      val bookingService = new BookingService(mockBookingRepository)
      when(mockBookingRepository.isInitialize).thenReturn(true)
      when(mockBookingRepository.getRemainingTables).thenReturn(2)
      when(mockBookingRepository.getNextFreeTableId).thenReturn(1)
      doNothing().when(mockBookingRepository).updateTable(any(), any())
      when(mockBookingRepository.insertBooking(any())).thenReturn(1)
      bookingService.bookTables(5) mustBe BookingResult(success = true, bookingId = 1, numberOfBookedTables = 2, numberOfRemainingTables = 2)
    }
  }

  "cancelBooking" should {
    "return false if restaurant is not already initialized." in {
      val mockBookingRepository = mock[BookingRepository]
      val bookingService = new BookingService(mockBookingRepository)
      when(mockBookingRepository.isInitialize).thenReturn(false)
      bookingService.cancelBooking(1) mustBe CancelBookingResult(success = false, message = "Restaurant is not initialized.")
    }

    "return false if booking is not found." in {
      val mockBookingRepository = mock[BookingRepository]
      val bookingService = new BookingService(mockBookingRepository)
      when(mockBookingRepository.isInitialize).thenReturn(true)
      when(mockBookingRepository.hasBooking(1)).thenReturn(false)
      bookingService.cancelBooking(1) mustBe CancelBookingResult(success = false, message = "Booking not found")
    }

    "return true if restaurant has cancelled successfully." in {
      val mockBookingRepository = mock[BookingRepository]
      val bookingService = new BookingService(mockBookingRepository)
      when(mockBookingRepository.isInitialize).thenReturn(true)
      when(mockBookingRepository.hasBooking(1)).thenReturn(true)
      when(mockBookingRepository.getBooking(1)).thenReturn(Booking(1, List(1, 2)))
      doNothing().when(mockBookingRepository).deleteBooking(Booking(1, List(1, 2)))
      doNothing().when(mockBookingRepository).updateTable(any(), any())
      when(mockBookingRepository.getRemainingTables).thenReturn(2)
      bookingService.cancelBooking(1) mustBe CancelBookingResult(success = true, numberOfFreedTables = 2, numberOfRemainingTables = 2)
    }
  }
}