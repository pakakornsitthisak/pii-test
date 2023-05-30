package controllers

import controllers.v1.BookingController
import models.{BookingResult, CancelBookingResult, InitResult}
import org.mockito.Matchers.any
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.Play.materializer
import play.api.libs.json.Json
import scala.collection.mutable.{ListBuffer => mutableList, Map => mutableMap}
import play.api.test._
import play.api.test.Helpers._
import services.BookingService
import models.Booking

class BookingControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting with MockitoSugar {

  "BookingController GET" should {
    "render the index page from the application" in {
      val controller = inject[BookingController]
      val home = controller.index().apply(FakeRequest(GET, "/"))
      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include("Welcome to Play")
    }
  }

  "initRestaurant" should {
    "return success if good request." in {
      val mockBookingService = mock[BookingService]
      val controller = new BookingController(mockBookingService, stubControllerComponents())
      val jsonStr = """{"number_of_tables": 5}"""
      val jsonBody = Json.parse(jsonStr)
      val request = FakeRequest(POST, "/initRestaurant")
        .withHeaders("Content-Type" -> "application/json")
        .withBody(jsonBody)
      when(mockBookingService.initializeRestaurant(any())).thenReturn(InitResult(success = true))
      val result = controller.initRestaurant().apply(request)
      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("""{"success":true,"message":""}""")
    }

    "return fail if bad request." in {
      val mockBookingService = mock[BookingService]
      val controller = new BookingController(mockBookingService, stubControllerComponents())
      val jsonStr = """{}"""
      val jsonBody = Json.parse(jsonStr)
      val request = FakeRequest(POST, "/initRestaurant")
        .withHeaders("Content-Type" -> "application/json")
        .withBody(jsonBody)
      when(mockBookingService.initializeRestaurant(any())).thenReturn(InitResult(success = true))
      val result = controller.initRestaurant().apply(request)
      status(result) mustBe BAD_REQUEST
    }
  }

  "reserveTables" should {
    "return success if good request." in {
      val mockBookingService = mock[BookingService]
      val controller = new BookingController(mockBookingService, stubControllerComponents())
      val jsonStr = """{"number_of_people": 5}"""
      val jsonBody = Json.parse(jsonStr)
      val request = FakeRequest(POST, "/reserveTables")
        .withHeaders("Content-Type" -> "application/json")
        .withBody(jsonBody)
      when(mockBookingService.bookTables(any())).thenReturn(BookingResult(success = true, bookingId = 1, numberOfBookedTables = 2, numberOfRemainingTables = 3))
      val result = controller.reserveTables().apply(request)
      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("""{"success":true,"message":"","booking_id":1,"number_of_booked_tables":2,"number_of_remaining_tables":3}""")
    }

    "return fail if bad request." in {
      val mockBookingService = mock[BookingService]
      val controller = new BookingController(mockBookingService, stubControllerComponents())
      val jsonStr = """{}"""
      val jsonBody = Json.parse(jsonStr)
      val request = FakeRequest(POST, "/reserveTables")
        .withHeaders("Content-Type" -> "application/json")
        .withBody(jsonBody)
      val result = controller.reserveTables().apply(request)
      status(result) mustBe BAD_REQUEST
    }
  }

  "cancelBooking" should {
    "return success if good request." in {
      val mockBookingService = mock[BookingService]
      val controller = new BookingController(mockBookingService, stubControllerComponents())
      val jsonStr = """{"booking_id": 1}"""
      val jsonBody = Json.parse(jsonStr)
      val request = FakeRequest(POST, "/cancelBooking")
        .withHeaders("Content-Type" -> "application/json")
        .withBody(jsonBody)
      when(mockBookingService.cancelBooking(any())).thenReturn(CancelBookingResult(success = true, numberOfFreedTables = 2, numberOfRemainingTables = 3))
      val result = controller.cancelBooking().apply(request)
      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("""{"success":true,"message":"","number_of_freed_tables":2,"number_of_remaining_tables":3}""")
    }

    "return fail if bad request." in {
      val mockBookingService = mock[BookingService]
      val controller = new BookingController(mockBookingService, stubControllerComponents())
      val jsonStr = """{}"""
      val jsonBody = Json.parse(jsonStr)
      val request = FakeRequest(POST, "/cancelBooking")
        .withHeaders("Content-Type" -> "application/json")
        .withBody(jsonBody)
      val result = controller.cancelBooking().apply(request)
      status(result) mustBe BAD_REQUEST
    }
  }

  "getStatus" should {
    "return success with no result." in {
      val mockBookingService = mock[BookingService]
      val controller = new BookingController(mockBookingService, stubControllerComponents())
      val jsonStr = """{}"""
      val jsonBody = Json.parse(jsonStr)
      val request = FakeRequest(GET, "/getStatus")
        .withHeaders("Content-Type" -> "application/json")
        .withBody(jsonBody)
      when(mockBookingService.getAllTables).thenReturn(mutableMap())
      when(mockBookingService.getAllBooking).thenReturn(mutableList())
      val result = controller.getStatus().apply(request)
      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("""{"tables":[],"bookings":[]}""")
    }

    "return success with results." in {
      val mockBookingService = mock[BookingService]
      val controller = new BookingController(mockBookingService, stubControllerComponents())
      val jsonStr = """{}"""
      val jsonBody = Json.parse(jsonStr)
      val request = FakeRequest(GET, "/getStatus")
        .withHeaders("Content-Type" -> "application/json")
        .withBody(jsonBody)
      when(mockBookingService.getAllTables).thenReturn(mutableMap(1 -> false, 2 -> true, 3 -> true))
      when(mockBookingService.getAllBooking).thenReturn(mutableList(Booking(1, List(2, 3))))
      val result = controller.getStatus().apply(request)
      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("""{"tables":[[1,false],[2,true],[3,true]],"bookings":[{"id":1,"tables":[2,3]}]}""")
    }
  }
}
