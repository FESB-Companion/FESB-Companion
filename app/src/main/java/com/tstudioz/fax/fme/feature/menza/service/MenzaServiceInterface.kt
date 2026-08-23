package com.tstudioz.fax.fme.feature.menza.service

import com.tstudioz.fax.fme.networking.NetworkServiceResult

interface MenzaServiceInterface {

    suspend fun fetchMenza(place: String): NetworkServiceResult.MenzaResult

}