package services

import models.{User, UserInfo}
import org.mockito.Mockito._
import org.scalatest.concurrent.ScalaFutures.whenReady
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play._
import play.api.db.Database
import play.api.mvc.RequestHeader
import play.api.mvc.Results.{Ok, Unauthorized}
import play.api.test.FakeRequest
import play.api.test.Helpers.{POST, stubControllerComponents}
import repositories.UserRepositoryImpl

import java.util.UUID
import scala.concurrent.{ExecutionContext, Future}

class AuthServiceSpec extends PlaySpec with MockitoSugar {
  implicit val executionContext = ExecutionContext.Implicits.global
  val authService = new AuthService(stubControllerComponents())

  "extractUser" should {
    "return valid user matching with token" in {
      val request = FakeRequest(POST, "/users")
        .withHeaders("Content-Type" -> "application/json", "Authorization" -> "auth1")
      authService.extractUser(request) mustBe Some(UserInfo("admin", "password", "auth1", true))
    }

    "return no user" in {
      val request = FakeRequest(POST, "/users")
        .withHeaders("Content-Type" -> "application/json", "Authorization" -> "auth")
      authService.extractUser(request) mustBe None
    }
  }

  "withUser" should {
    "allow request to get executed by block" in {
      val request = FakeRequest(POST, "/users")
        .withHeaders("Content-Type" -> "application/json", "Authorization" -> "auth1")
      whenReady(authService.withUser(_ => { Future(Ok) })(request)) {
        result => result mustBe Ok
      }
    }

    "return unauthorized to reject request" in {
      val request = FakeRequest(POST, "/users")
        .withHeaders("Content-Type" -> "application/json", "Authorization" -> "auth")
      whenReady(authService.withUser(_ => { Future(Ok) })(request)) {
        result => result mustBe Unauthorized("Unauthorized to perform operation")
      }
    }
  }
}