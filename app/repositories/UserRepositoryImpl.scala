package repositories

import java.util.UUID
import anorm._
import dao.UserRepositoryDao
import models.{APIError, User}

import javax.inject.Inject
import play.api.db.Database

import scala.concurrent.{ExecutionContext, Future}

class UserRepositoryImpl @Inject()(db: Database,
                                    databaseExecutionContext: ExecutionContext,
                                   userRepositoryDao: UserRepositoryDao)
  extends UserRepository {

  val parser: RowParser[User] = Macro.namedParser[User]

  override def getUsers(): Future[List[User]] = {
    Future {
      db.withConnection { implicit conn =>
        userRepositoryDao.get()
      }
    }(databaseExecutionContext)
  }

  override def getUserById(id: UUID): Future[Option[User]] = {
    Future {
      db.withConnection { implicit conn =>
        userRepositoryDao.getBy(id)
      }
    }(databaseExecutionContext)
  }

  override def addUser(user: User): Future[Either[APIError, User]] = {
    Future {
      db.withConnection { implicit conn =>
        userRepositoryDao.insert(user)
      }
    }(databaseExecutionContext)
  }

  override def updateUser(id: UUID, user: User): Future[Int] = {
    Future {
      db.withConnection { implicit conn =>
        userRepositoryDao.update(id, user)
      }
    }(databaseExecutionContext)
  }

  override def deleteUserById(id: UUID): Future[Int] = {
    Future {
      db.withConnection { implicit conn =>
        userRepositoryDao.delete(id)
      }
    }(databaseExecutionContext)
  }
}
