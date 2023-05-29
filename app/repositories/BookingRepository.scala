package repositories

import com.google.inject.Inject
import models.Booking

import scala.collection.mutable.{Map => mutableMap, ListBuffer => mutableList}
import scala.concurrent.ExecutionContext

class BookingRepository @Inject()(implicit ec: ExecutionContext) {

  private val tables: mutableMap[Int, Boolean] = mutableMap()
  private val bookings: mutableList[Booking] = mutableList()
  private var isInitailize = false
  private var bookingId = 1

  def clear() = {
    tables.clear()
    bookings.clear()
    isInitailize = true
    bookingId = 1
  }

  def isInitialize = isInitailize

  def getAllTables = {
    tables
  }

  def insertTable(tableId: Int) = {
    tables += tableId -> false
  }

  def updateTable(tableId: Int, reserved: Boolean) = {
    tables.put(tableId, reserved)
  }

  def getNextFreeTableId = {
    tables.filter(!_._2).head._1
  }

  def getRemainingTables = {
    tables.count(t => !t._2)
  }

  def getAllBookings = {
    bookings
  }

  def insertBooking(tables: List[Int]) = {
    val currentBookingId = bookingId
    bookings += Booking(bookingId, tables)
    bookingId += 1
    currentBookingId
  }

  def deleteBooking(booking: Booking) = {
    bookings -= booking
  }

  def getBooking(bookingId: Int) = {
    bookings.filter(b => b.id == bookingId).head
  }

  def hasBooking(bookingId: Int) = {
    bookings.exists(b => b.id == bookingId)
  }
}
