package controllers

import controllers.v1.{AuthController, UserController}
import models.User
import org.mockito.Mockito.when
import org.scalatest.concurrent.ScalaFutures.whenReady
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import org.mockito.Matchers.any
import play.api.libs.json.Json
import play.api.mvc.Results.{BadRequest, Ok, Unauthorized}
import play.api.test.Helpers._
import play.api.test._
import services.{AuthService, UserService}

import java.util.UUID
import scala.concurrent.{ExecutionContext, Future}

class UserControllerSpec extends PlaySpec with MockitoSugar {
  implicit val executionContext = ExecutionContext.Implicits.global
  val mockAuthService = new AuthService(stubControllerComponents())
  val mockUserService = mock[UserService]
  val userController = new UserController(stubControllerComponents(), mockUserService, mockAuthService)

  "getUsers" should {
    "return all users" in {
      val request = FakeRequest(GET, "/users")
        .withHeaders("Content-Type" -> "application/json", "Authorization" -> "auth1")
      val mockUuid = UUID.randomUUID()
      val testUser = User(id = mockUuid, name = "name", email = "email")
      when(mockUserService.getUsers()).thenReturn(Future(List(testUser)))
      val result = userController.getUsers().apply(request)
      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include(""""name":"name","email":"email"""")
      contentAsString(result) must include(s""""id":"${mockUuid}"""")
    }
  }

  "getUsersByPartialName" should {
    "return all users with name like" in {
      val request = FakeRequest(GET, "/users?name=name")
        .withHeaders("Content-Type" -> "application/json", "Authorization" -> "auth1")
      val mockUuid = UUID.randomUUID()
      val testUser = User(id = mockUuid, name = "name", email = "email")
      when(mockUserService.getUsersByPartialName("name")).thenReturn(Future(List(testUser)))
      val result = userController.getUsersByPartialName().apply(request)
      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include(""""name":"name","email":"email"""")
      contentAsString(result) must include(s""""id":"${mockUuid}"""")
    }
  }

  "getUserById" should {
    "return user with id" in {
      val mockUuid = UUID.randomUUID()
      val request = FakeRequest(GET, s"/users")
        .withHeaders("Content-Type" -> "application/json", "Authorization" -> "auth1")
      val testUser = User(id = mockUuid, name = "name", email = "email")
      when(mockUserService.getUserById(mockUuid)).thenReturn(Future(Some(testUser)))
      val result = userController.getUserById(mockUuid).apply(request)
      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include(""""name":"name","email":"email"""")
      contentAsString(result) must include(s""""id":"${mockUuid}"""")
    }
  }

  "createUser" should {
    "create user successfully" in {
      val mockUuid = UUID.randomUUID()
      val jsonStr = """{"name": "name", "email": "email"}"""
      val jsonBody = Json.parse(jsonStr)
      val request = FakeRequest(GET, s"/users")
        .withHeaders("Content-Type" -> "application/json", "Authorization" -> "auth1")
        .withBody(jsonBody)
      val testUser = User(id = mockUuid, name = "name", email = "email")
      when(mockUserService.createUser(any[User])).thenReturn(Future(Right(testUser)))
      val result = userController.createUser().apply(request)
      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include(""""name":"name","email":"email"""")
      contentAsString(result) must include(s""""id":"${mockUuid}"""")
    }
  }

  "updateUserById" should {
    "update user successfully" in {
      val mockUuid = UUID.randomUUID()
      val jsonStr = s"""{"id": "${mockUuid}", "name": "name", "email": "email"}"""
      val jsonBody = Json.parse(jsonStr)
      val request = FakeRequest(GET, s"/users")
        .withHeaders("Content-Type" -> "application/json", "Authorization" -> "auth1")
        .withBody(jsonBody)
      val testUser = User(id = mockUuid, name = "name", email = "email")
      when(mockUserService.updateUserById(mockUuid, testUser)).thenReturn(Future(1))
      val result = userController.updateUserById(mockUuid).apply(request)
      status(result) mustBe OK
      contentType(result) mustBe  Some("text/plain")
    }

    "fail if userService return 0" in {
      val mockUuid = UUID.randomUUID()
      val jsonStr = s"""{"id": "${mockUuid}", "name": "name", "email": "email"}"""
      val jsonBody = Json.parse(jsonStr)
      val request = FakeRequest(GET, s"/users")
        .withHeaders("Content-Type" -> "application/json", "Authorization" -> "auth1")
        .withBody(jsonBody)
      val testUser = User(id = mockUuid, name = "name", email = "email")
      when(mockUserService.updateUserById(mockUuid, testUser)).thenReturn(Future(0))
      val result = userController.updateUserById(mockUuid).apply(request)
      status(result) mustBe BAD_REQUEST
      contentType(result) mustBe Some("text/plain")
    }

    "fail if bad request" in {
      val mockUuid = UUID.randomUUID()
      val jsonStr = s"""{"s1": "${mockUuid}", "s2": "name", "s3": "email"}"""
      val jsonBody = Json.parse(jsonStr)
      val request = FakeRequest(GET, s"/users")
        .withHeaders("Content-Type" -> "application/json", "Authorization" -> "auth1")
        .withBody(jsonBody)
      val testUser = User(id = mockUuid, name = "name", email = "email")
      when(mockUserService.updateUserById(mockUuid, testUser)).thenReturn(Future(0))
      val result = userController.updateUserById(mockUuid).apply(request)
      status(result) mustBe BAD_REQUEST
      contentType(result) mustBe Some("text/plain")
    }
  }

  "deleteUserById" should {
    "delete user successfully" in {
      val mockUuid = UUID.randomUUID()
      val request = FakeRequest(GET, s"/users")
        .withHeaders("Content-Type" -> "application/json", "Authorization" -> "auth1")
      when(mockUserService.deleteUserById(mockUuid)).thenReturn(Future(1))
      val result = userController.deleteUserById(mockUuid).apply(request)
      status(result) mustBe OK
      contentType(result) mustBe Some("text/plain")
    }

    "fail if userService return 0" in {
      val mockUuid = UUID.randomUUID()
      val request = FakeRequest(GET, s"/users")
        .withHeaders("Content-Type" -> "application/json", "Authorization" -> "auth1")
      when(mockUserService.deleteUserById(mockUuid)).thenReturn(Future(0))
      val result = userController.deleteUserById(mockUuid).apply(request)
      status(result) mustBe BAD_REQUEST
      contentType(result) mustBe Some("text/plain")
    }
  }
}
