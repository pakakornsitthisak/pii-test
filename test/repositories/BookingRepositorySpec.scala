package controllers

import controllers.v1.BookingController
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._
import repositories.BookingRepository
import models.Booking


class BookingRepositorySpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  "clear" should {
    val bookingRepository = new BookingRepository()
    "work correctly" in {
      bookingRepository.isInitialize mustBe false
      bookingRepository.clear()
      bookingRepository.isInitialize mustBe true
      bookingRepository.getAllTables.isEmpty mustBe true
      bookingRepository.getAllBookings.isEmpty mustBe true
    }
  }

  "isInitialize" should {
    val bookingRepository = new BookingRepository()
    "work correctly" in {
      bookingRepository.isInitialize mustBe false
    }
  }

  "getAllTables" should {
    val bookingRepository = new BookingRepository()
    "work correctly" in {
      bookingRepository.getAllTables.isEmpty mustBe true
    }
  }

  "getAllBookings" should {
    val bookingRepository = new BookingRepository()
    "work correctly" in {
      bookingRepository.getAllBookings.isEmpty mustBe true
    }
  }

  "insertTable" should {
    val bookingRepository = new BookingRepository()
    "work correctly" in {
      bookingRepository.getAllTables.isEmpty mustBe true
      bookingRepository.insertTable(1)
      bookingRepository.getAllTables.size mustBe 1
      bookingRepository.getAllTables.head mustBe (1, false)
      bookingRepository.insertTable(2)
      bookingRepository.getAllTables.size mustBe 2
      bookingRepository.getAllTables.last mustBe(2, false)
    }
  }

  "updateTable" should {
    val bookingRepository = new BookingRepository()
    "work correctly" in {
      bookingRepository.getAllTables.isEmpty mustBe true
      bookingRepository.insertTable(1)
      bookingRepository.getAllTables.size mustBe 1
      bookingRepository.getAllTables.head mustBe(1, false)
      bookingRepository.updateTable(1, true)
      bookingRepository.getAllTables.size mustBe 1
      bookingRepository.getAllTables.head mustBe(1, true)
    }
  }

  "getNextFreeTableId" should {
    val bookingRepository = new BookingRepository()
    "work correctly" in {
      bookingRepository.getAllTables.isEmpty mustBe true
      bookingRepository.insertTable(1)
      bookingRepository.getAllTables.size mustBe 1
      bookingRepository.getNextFreeTableId mustBe 1
      bookingRepository.updateTable(1, true)
      bookingRepository.insertTable(2)
      bookingRepository.getAllTables.size mustBe 2
      bookingRepository.getNextFreeTableId mustBe 2
      bookingRepository.insertTable(3)
      bookingRepository.insertTable(4)
      bookingRepository.updateTable(2, true)
      bookingRepository.updateTable(3, true)
      bookingRepository.getNextFreeTableId mustBe 4
      bookingRepository.updateTable(1, false)
      bookingRepository.getNextFreeTableId mustBe 1
    }
  }

  "getRemainingTables" should {
    val bookingRepository = new BookingRepository()
    "work correctly" in {
      bookingRepository.getAllTables.isEmpty mustBe true
      bookingRepository.insertTable(1)
      bookingRepository.getRemainingTables mustBe 1
      bookingRepository.updateTable(1, true)
      bookingRepository.insertTable(2)
      bookingRepository.getRemainingTables mustBe 1
      bookingRepository.insertTable(3)
      bookingRepository.insertTable(4)
      bookingRepository.updateTable(2, true)
      bookingRepository.updateTable(3, true)
      bookingRepository.getRemainingTables mustBe 1
      bookingRepository.updateTable(1, false)
      bookingRepository.getRemainingTables mustBe 2
    }
  }

  "insertBooking" should {
    val bookingRepository = new BookingRepository()
    "work correctly" in {
      bookingRepository.getAllBookings.isEmpty mustBe true
      bookingRepository.insertBooking(List(1, 2))
      bookingRepository.getAllBookings.head mustBe Booking(1, List(1, 2))
      bookingRepository.insertBooking(List(3, 4))
      bookingRepository.getAllBookings.last mustBe Booking(2, List(3, 4))
    }
  }

  "deleteBooking" should {
    val bookingRepository = new BookingRepository()
    "work correctly" in {
      bookingRepository.getAllBookings.isEmpty mustBe true
      bookingRepository.insertBooking(List(1, 2))
      bookingRepository.getAllBookings.head mustBe Booking(1, List(1, 2))
      bookingRepository.deleteBooking(Booking(1, List(1, 2)))
      bookingRepository.getAllBookings.isEmpty
      bookingRepository.insertBooking(List(1, 2))
      bookingRepository.insertBooking(List(3, 4))
      bookingRepository.getAllBookings.size mustBe 2
      bookingRepository.deleteBooking(Booking(2, List(1, 2)))
      bookingRepository.getAllBookings.size mustBe 1
      bookingRepository.getAllBookings.last mustBe Booking(3, List(3, 4))
      bookingRepository.deleteBooking(Booking(3, List(3, 4)))
      bookingRepository.getAllBookings.size mustBe 0
    }
  }

  "getBooking" should {
    val bookingRepository = new BookingRepository()
    "work correctly" in {
      bookingRepository.getAllBookings.isEmpty mustBe true
      bookingRepository.insertBooking(List(1, 2))
      bookingRepository.insertBooking(List(3, 4))
      bookingRepository.getAllBookings.size mustBe 2
      bookingRepository.getBooking(1) mustBe Booking(1, List(1, 2))
      bookingRepository.getBooking(2) mustBe Booking(2, List(3, 4))
    }
  }

  "hasBooking" should {
    val bookingRepository = new BookingRepository()
    "work correctly" in {
      bookingRepository.getAllBookings.isEmpty mustBe true
      bookingRepository.insertBooking(List(1, 2))
      bookingRepository.insertBooking(List(3, 4))
      bookingRepository.getAllBookings.size mustBe 2
      bookingRepository.hasBooking(1) mustBe true
      bookingRepository.hasBooking(2) mustBe true
      bookingRepository.hasBooking(3) mustBe false
    }
  }
}
