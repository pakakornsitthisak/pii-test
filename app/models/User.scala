package models

import play.api.libs.json.Json

import java.util.UUID

case class User(id: UUID, name: String, email: String)

object User {
  implicit val userFormat = Json.format[User]
}
