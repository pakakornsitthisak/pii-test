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
    isInitailize = false
    bookingId = 1
  }

  def isInitialize = isInitailize

  def insertTable(tableId: Int) = {
    tables += tableId -> false
  }

  def updateTable(tableId: Int, reserved: Boolean) = {
    tables.put(tableId, reserved)
  }

  def isTableReserved(tableId: Int) = {
    tables(tableId)
  }

  def getNextFreeTableId = {
    tables.filter(!_._2).head._1
  }

  def getRemainingTables = {
    tables.count(t => !t._2)
  }

  def getBookedTables = {
    tables.count(t => t._2)
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
    bookings(bookingId)
  }

  def hasBooking(bookingId: Int) = {
    bookings.contains(bookingId)
  }
}
