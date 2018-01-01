package ktln

import java.util.UUID;
import models.Activity
import models.Location
import models.User

class PacemakerAPI {

  var userIndex = hashMapOf<String, User>()
  var emailIndex = hashMapOf<String, User>()
  var activitiesIndex = hashMapOf<String, Activity>()
  var users = userIndex.values

  fun createUser(firstName: String, lastName: String, email: String, password: String): User {
    var user = User(firstName, lastName, email, password)
    userIndex[user.id] = user
    emailIndex[user.email] = user
    return user
  }

  fun deleteUsers() {
    userIndex.clear();
    emailIndex.clear()
  }

  fun getUser(id: String) = userIndex[id]
  fun getUserByEmail(email: String) = emailIndex[email]
}