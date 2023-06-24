package controllers.v1

import models.{CreateUser, User}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import services.UserService

import java.util.UUID
import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class UserController @Inject()(val controllerComponents: ControllerComponents,
                               userService: UserService)(implicit ec: ExecutionContext)
  extends BaseController {

  def getUsers(): Action[AnyContent] = Action.async { implicit request =>
    userService.getUsers().map { users =>
      Ok(Json.toJson(users))
    }
  }

  def getUserById(id: UUID): Action[AnyContent] = Action.async { implicit request =>
    userService.getUserById(id).map {
      case Some(user) => Ok(Json.toJson(user))
      case None => NotFound
    }
  }

  def createUser: Action[JsValue] = Action(parse.json).async { implicit request =>
    request.body
      .validate[CreateUser]
      .fold(
        errors => Future {
          BadRequest(errors.mkString)
        },
        newUser => {
          userService.createUser(User(UUID.randomUUID(), newUser.name, newUser.email)).map { res =>
            res match {
              case Left(_) => BadRequest("Error when creating user")
              case Right(user) => Ok(Json.toJson(user))
            }
          }
        }
      )
  }

  def updateUserById(id: UUID): Action[JsValue] = Action(parse.json).async { implicit request =>
    request.body
      .validate[User]
      .fold(
        errors => Future {
          BadRequest(errors.mkString)
        },
        user => {
          userService.updateUserById(id, user).map { res =>
            res match {
              case 1 => Ok("Ok")
              case 0 => BadRequest("Error when updating user")
            }
          }
        }
      )
  }

  def deleteUserById(id: UUID): Action[AnyContent] = Action.async { implicit request =>
    userService.deleteUserById(id).map { res =>
      res match {
        case 1 => Ok("Ok")
        case 0 => BadRequest("Error when deleting user")
      }
    }
  }
}
