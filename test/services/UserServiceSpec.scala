package services

import models.User
import org.mockito.Mockito._
import org.scalatest.concurrent.ScalaFutures.whenReady
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play._
import play.api.db.Database
import repositories.UserRepositoryImpl

import java.util.UUID
import scala.concurrent.{ExecutionContext, Future}

class UserServiceSpec extends PlaySpec with MockitoSugar {

  "getUsers" should {
    "return all users from repo" in {
      implicit val executionContext = ExecutionContext.Implicits.global
      val mockDatabase = mock[Database]
      val mockUserRepository = mock[UserRepositoryImpl]
      val userService = new UserService(mockDatabase, executionContext, mockUserRepository)
      val mockUser = User(id = UUID.randomUUID(), name = "", email = "")
      when(mockUserRepository.getUsers).thenReturn(Future(List(mockUser)))
      whenReady(userService.getUsers()) {
        result => result must be equals List(mockUser)
      }
    }
  }

  "getUsersByPartialName" should {
    "return all users matching name" in {
      implicit val executionContext = ExecutionContext.Implicits.global
      val mockDatabase = mock[Database]
      val mockUserRepository = mock[UserRepositoryImpl]
      val userService = new UserService(mockDatabase, executionContext, mockUserRepository)
      val mockUser = User(id = UUID.randomUUID(), name = "name", email = "")
      when(mockUserRepository.getUsersByPartialName("name")).thenReturn(Future(List(mockUser)))
      whenReady(userService.getUsersByPartialName("name")) {
        result => result must be equals List(mockUser)
      }
    }
  }

  "getUserById" should {
    "return user by id" in {
      implicit val executionContext = ExecutionContext.Implicits.global
      val mockDatabase = mock[Database]
      val mockUserRepository = mock[UserRepositoryImpl]
      val userService = new UserService(mockDatabase, executionContext, mockUserRepository)
      val mockId = UUID.randomUUID()
      val mockUser = User(id = mockId, name = "", email = "")
      when(mockUserRepository.getUserById(mockId)).thenReturn(Future(Some(mockUser)))
      whenReady(userService.getUserById(mockId)) {
        result => result must be equals List(mockUser)
      }
    }
  }

  "createUser" should {
    "return create user" in {
      implicit val executionContext = ExecutionContext.Implicits.global
      val mockDatabase = mock[Database]
      val mockUserRepository = mock[UserRepositoryImpl]
      val userService = new UserService(mockDatabase, executionContext, mockUserRepository)
      val mockId = UUID.randomUUID()
      val mockUser = User(id = mockId, name = "", email = "")
      when(mockUserRepository.addUser(mockUser)).thenReturn(Future(Right(mockUser)))
      whenReady(userService.createUser(mockUser)) {
        result => result must be equals Right(mockUser)
      }
    }
  }

  "createUser" should {
    "return update user" in {
      implicit val executionContext = ExecutionContext.Implicits.global
      val mockDatabase = mock[Database]
      val mockUserRepository = mock[UserRepositoryImpl]
      val userService = new UserService(mockDatabase, executionContext, mockUserRepository)
      val mockId = UUID.randomUUID()
      val mockUser = User(id = mockId, name = "", email = "")
      when(mockUserRepository.updateUser(mockId, mockUser)).thenReturn(Future(1))
      whenReady(userService.updateUserById(mockId, mockUser)) {
        result => result must be equals 1
      }
    }
  }

  "deleteUserById" should {
    "return delete user" in {
      implicit val executionContext = ExecutionContext.Implicits.global
      val mockDatabase = mock[Database]
      val mockUserRepository = mock[UserRepositoryImpl]
      val userService = new UserService(mockDatabase, executionContext, mockUserRepository)
      val mockId = UUID.randomUUID()
      when(mockUserRepository.deleteUserById(mockId)).thenReturn(Future(1))
      whenReady(userService.deleteUserById(mockId)) {
        result => result must be equals 1
      }
    }
  }
}