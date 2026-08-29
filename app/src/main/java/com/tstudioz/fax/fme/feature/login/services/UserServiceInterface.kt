package com.tstudioz.fax.fme.feature.login.services

import com.tstudioz.fax.fme.networking.NetworkServiceResult

interface UserServiceInterface {

    suspend fun loginUser(username: String, password: String): NetworkServiceResult.LoginResult

}

