package com.tstudioz.fax.fme.feature.home.services

import com.tstudioz.fax.fme.networking.NetworkServiceResult

interface WeatherServiceInterface {

    suspend fun fetchWeatherDetails(): NetworkServiceResult.WeatherResult
}