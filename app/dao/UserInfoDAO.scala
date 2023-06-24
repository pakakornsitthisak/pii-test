package dao

import models.{LoginRequest, UserInfo}

object UserInfoDAO {

  // To-Do: get users from database
  def userList: List[UserInfo] = {
    val adminUser = UserInfo("admin", "password", "auth1", true)
    val basicUser = UserInfo("basic_user", "password", "auth2", false)
    List(adminUser, basicUser)
  }

  // create Map of auth token, and User
  final val usersMap = userList.map(user => (user.authToken, user)).toMap

  def getUserInfo(username: String): Option[UserInfo] = {
    usersMap.get(username)
  }

  def getAuthToken(req: LoginRequest): Option[String] = {
    usersMap.find(_._2.username == req.username).map(_._1)
  }
}
