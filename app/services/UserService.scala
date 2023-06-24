package services

import com.google.inject.Inject
import models.{APIError, User}
import play.api.db.Database
import repositories.{UserRepositoryImpl}

import java.util.UUID
import scala.concurrent.{ExecutionContext, Future}

class UserService @Inject()(db: Database,
                           databaseExecutionContext: ExecutionContext,
                            userRepository: UserRepositoryImpl) {
  def getUsers(): Future[List[User]] = {
    userRepository.getUsers()
  }

  def getUsersByPartialName(name: String): Future[List[User]] = {
    userRepository.getUsersByPartialName(name)
  }

  def getUserById(id: UUID): Future[Option[User]] = {
    userRepository.getUserById(id)
  }

  def createUser(user: User): Future[Either[APIError, User]] = {
    userRepository.addUser(user)
  }

  def updateUserById(id: UUID, user: User): Future[Int] = {
    userRepository.updateUser(id, user)
  }

  def deleteUserById(id: UUID): Future[Int] = {
    userRepository.deleteUserById(id)
  }
}
