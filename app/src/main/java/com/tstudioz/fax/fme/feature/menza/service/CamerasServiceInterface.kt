package com.tstudioz.fax.fme.feature.menza.service

import com.tstudioz.fax.fme.feature.menza.utils.CamerasResult

interface CamerasServiceInterface {

    suspend fun getCameraImageUrls(path: String): CamerasResult.GetCamerasResult

}