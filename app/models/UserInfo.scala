package models

import play.api.libs.json.Json

case class UserInfo(username: String,
                password: String,
                authToken: String = "",
                isAdmin: Boolean = false)

object UserInfo {
  implicit val userInfoFormat = Json.format[UserInfo]
}
