package dao

import anorm.{Macro, RowParser, SQL}
import models.{APIError, User}
import org.postgresql.util.PSQLException
import play.api.Logging
import play.api.db.Database

import java.sql.Connection
import java.util.UUID
import javax.inject.Inject

class UserRepositoryDao @Inject()(db: Database) extends Logging {
  val parser: RowParser[User] = Macro.namedParser[User]

  def get()(implicit conn: Connection): List[User] = {
    SQL("SELECT * FROM users").as(parser.*)
  }

  def getBy(id: UUID)(implicit conn: Connection): Option[User] = {
    SQL("SELECT id, name, email FROM users WHERE id = {id}::uuid")
      .on("id" -> id)
      .as(parser.singleOpt)
  }

  def insert(user: User)(implicit conn: Connection): Either[APIError, User] = {
    try {
      SQL("INSERT INTO users (id, name, email) VALUES ({id}::uuid, {name}, {email})")
        .on("id" -> user.id, "name" -> user.name, "email" -> user.email)
        .execute()
      Right(user)
    } catch {
      case e: PSQLException => {
        logger.error(e.getMessage())
        Left(APIError("PSQL error when inserting user"))
      }
      case e: Exception => {
        logger.error(e.getMessage())
        Left(APIError("Unknown error when inserting user"))
      }
    }
  }

  def update(id: UUID, user: User)(implicit conn: Connection): Int = {
    SQL("UPDATE users SET name = {name}, email = {email} WHERE id = {id}::uuid")
      .on("id" -> user.id, "name" -> user.name, "email" -> user.email)
      .executeUpdate()
  }

  def delete(id: UUID)(implicit conn: Connection): Int = {
    SQL("DELETE FROM users WHERE id = {id}::uuid").on("id" -> id).executeUpdate()
  }
}
