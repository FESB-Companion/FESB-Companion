package com.tstudioz.fax.fme.user

import com.tstudioz.fax.fme.user.models.User
import com.tstudioz.fax.fme.user.models.UserRepositoryResult

interface UserRepositoryInterface {

    suspend fun attemptLogin(username: String, password: String): UserRepositoryResult.LoginResult

    suspend fun insertDummyUser()

    suspend fun getCurrentUserName(): String

    suspend fun getCurrentUser(): User

    suspend fun deleteAllUserData()

}
