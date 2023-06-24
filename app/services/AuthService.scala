package services

import dao.UserInfoDAO
import models.UserInfo
import play.api.mvc._

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class AuthService @Inject()(val controllerComponents: ControllerComponents)(
    implicit ec: ExecutionContext)
    extends BaseController {
  def extractUser(req: RequestHeader): Option[UserInfo] = {
    val authHeader = req.headers.get("Authorization")
    authHeader
      .flatMap(authHeader => UserInfoDAO.getUserInfo(authHeader))
  }

  def withUser[T](block: UserInfo => Future[Result])(
      implicit request: Request[Any]): Future[Result] = {
    val user = extractUser(request)
    user.map(block).getOrElse(Future { Unauthorized("Unauthorized to perform operation") })
  }
}
