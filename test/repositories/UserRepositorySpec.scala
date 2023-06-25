package repositories

import dao.UserRepositoryDao
import models.{APIError, User}
import org.mockito.Mockito._
import org.scalatest.concurrent.ScalaFutures.whenReady
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play._
import play.api.db.Database

import java.sql.Connection
import java.util.UUID
import scala.concurrent.{ExecutionContext, Future}

class UserRepositorySpec extends PlaySpec with MockitoSugar {
  implicit val executionContext = ExecutionContext.Implicits.global
  val mockDatabase = mock[Database]
  implicit val mockConnection = mock[Connection]
  val mockUserRepositoryDao = mock[UserRepositoryDao]
  val userUserRepositoryImpl = new UserRepositoryImpl(mockDatabase, executionContext, mockUserRepositoryDao)
  "getUsers" should {
    "return all users from dao" in {
      val mockId = UUID.randomUUID()
      val mockUser = User(id = mockId, name = "", email = "")
      when(mockUserRepositoryDao.get()).thenReturn(List(mockUser))
      whenReady(userUserRepositoryImpl.getUsers()) {
        result => result must be equals List(mockUser)
      }
    }
  }

  "getUsersByPartialName" should {
    "return users from dao matching name" in {
      val mockId = UUID.randomUUID()
      val mockUser = User(id = mockId, name = "name", email = "")
      when(mockUserRepositoryDao.getByPartialName("name")).thenReturn(List(mockUser))
      whenReady(userUserRepositoryImpl.getUsersByPartialName("name")) {
        result => result must be equals List(mockUser)
      }
    }
  }

  "getUserById" should {
    "return user by id from dao" in {
      val mockId = UUID.randomUUID()
      val mockUser = User(id = mockId, name = "", email = "")
      when(mockUserRepositoryDao.getBy(mockId)).thenReturn(Some(mockUser))
      whenReady(userUserRepositoryImpl.getUserById(mockId)) {
        result => result must be equals Some(mockUser)
      }
    }
  }

  "addUser" should {
    "return success if added successfully" in {
      val mockId = UUID.randomUUID()
      val mockUser = User(id = mockId, name = "", email = "")
      when(mockUserRepositoryDao.insert(mockUser)).thenReturn(Right(mockUser))
      whenReady(userUserRepositoryImpl.addUser(mockUser)) {
        result => result must be equals Right(mockUser)
      }
    }

    "return failed if failed to add" in {
      val mockId = UUID.randomUUID()
      val mockUser = User(id = mockId, name = "", email = "")
      when(mockUserRepositoryDao.insert(mockUser)).thenReturn(Left(APIError("")))
      whenReady(userUserRepositoryImpl.addUser(mockUser)) {
        result => result must be equals Left(APIError(""))
      }
    }
  }

  "updateUser" should {
    "return success if updated successfully" in {
      val mockId = UUID.randomUUID()
      val mockUser = User(id = mockId, name = "", email = "")
      when(mockUserRepositoryDao.update(mockId, mockUser)).thenReturn(1)
      whenReady(userUserRepositoryImpl.updateUser(mockId, mockUser)) {
        result => result must be equals 1
      }
    }

    "return failed if failed to update" in {
      val mockId = UUID.randomUUID()
      val mockUser = User(id = mockId, name = "", email = "")
      when(mockUserRepositoryDao.update(mockId, mockUser)).thenReturn(0)
      whenReady(userUserRepositoryImpl.updateUser(mockId, mockUser)) {
        result => result must be equals 0
      }
    }
  }

  "delete" should {
    "return success if deleted successfully" in {
      val mockId = UUID.randomUUID()
      when(mockUserRepositoryDao.delete(mockId)).thenReturn(1)
      whenReady(userUserRepositoryImpl.deleteUserById(mockId)) {
        result => result must be equals 1
      }
    }

    "return failed if failed to delete" in {
      val mockId = UUID.randomUUID()
      when(mockUserRepositoryDao.delete(mockId)).thenReturn(0)
      whenReady(userUserRepositoryImpl.deleteUserById(mockId)) {
        result => result must be equals 0
      }
    }
  }
}