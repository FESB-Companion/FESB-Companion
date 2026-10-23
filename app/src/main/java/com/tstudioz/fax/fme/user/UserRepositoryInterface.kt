package com.tstudioz.fax.fme.user

import com.tstudioz.fax.fme.user.models.User
import com.tstudioz.fax.fme.user.models.UserRepositoryResult
import kotlinx.coroutines.flow.Flow

interface UserRepositoryInterface {

    val showGithubMessage: Flow<Boolean>
    suspend fun attemptLogin(username: String, password: String): UserRepositoryResult.LoginResult

    suspend fun insertDummyUser()

    suspend fun getCurrentUserName(): String

    suspend fun getCurrentUser(): User

    suspend fun deleteAllUserData()

    suspend fun hideGithubMessage()
}
