package dao

import models.{APIError, User}
import org.mockito.Mockito._
import org.scalatest.concurrent.ScalaFutures.whenReady
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play._
import play.api.db.Database

import java.sql.Connection
import java.util.UUID
import scala.concurrent.ExecutionContext

class UserRepositoryDaoSpec extends PlaySpec with MockitoSugar {
  val mockDatabase = mock[Database]
  implicit val mockConnection = mock[Connection]
  val mockUserRepositoryDao = new UserRepositoryDao(mockDatabase)
  "getUsers" should {
    "return all users from dao" in {
//      val mockId = UUID.randomUUID()
//      val mockUser = User(id = mockId, name = "", email = "")
//      mockUserRepositoryDao.get() mustBe List(mockUser)
    }
  }
}