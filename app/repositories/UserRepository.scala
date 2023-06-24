package repositories

import com.google.inject.Inject
import models.{APIError, User}

import java.util.UUID
import scala.concurrent.Future
trait UserRepository {
  def getUsers(): Future[List[User]]
  def getUserById(id: UUID): Future[Option[User]]
  def addUser(user: User): Future[Either[APIError, User]]
  def updateUser(id: UUID, user: User): Future[Int]
  def deleteUserById(ID: UUID): Future[Int]
}
