package controllers

import controllers.v1.AuthController
import org.scalatest.concurrent.ScalaFutures.whenReady
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.libs.json.Json
import play.api.mvc.Results.{BadRequest, Unauthorized}
import play.api.test.Helpers._
import play.api.test._
import services.AuthService

import scala.concurrent.ExecutionContext

class AuthControllerSpec extends PlaySpec with MockitoSugar {
  implicit val executionContext = ExecutionContext.Implicits.global
  val mockAuthService = mock[AuthService]
  val authController = new AuthController(stubControllerComponents(), mockAuthService)

  "login" should {
    "return success if valid credentials." in {
      val jsonStr = """{"username": "basic_user", "password": "password"}"""
      val jsonBody = Json.parse(jsonStr)
      val request = FakeRequest(POST, "/login")
        .withHeaders("Content-Type" -> "application/json")
        .withBody(jsonBody)
      val result = authController.login(request)
      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")
      contentAsString(result) must include("""{"auth_token":"auth2"}""")
    }

    "return fail if invalid credentials." in {
      val jsonStr = """{"username": "test", "password": "test"}"""
      val jsonBody = Json.parse(jsonStr)
      val request = FakeRequest(POST, "/login")
        .withHeaders("Content-Type" -> "application/json")
        .withBody(jsonBody)
      val result = authController.login(request)
      status(result) mustBe UNAUTHORIZED
      contentType(result) mustBe Some("text/plain")
      whenReady(result) {
        res => res mustBe Unauthorized("Unauthorized to perform operation")
      }
    }

    "return fail if bad request" in {
      val jsonStr = """{"a": "test", "b": "test"}"""
      val jsonBody = Json.parse(jsonStr)
      val request = FakeRequest(POST, "/login")
        .withHeaders("Content-Type" -> "application/json")
        .withBody(jsonBody)
      val result = authController.login(request)
      status(result) mustBe BAD_REQUEST
      contentType(result) mustBe Some("text/plain")
    }
  }
}
