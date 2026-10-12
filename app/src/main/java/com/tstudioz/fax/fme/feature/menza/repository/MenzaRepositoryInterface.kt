package com.tstudioz.fax.fme.feature.menza.repository

import com.tstudioz.fax.fme.feature.menza.utils.MenzaLocationType
import com.tstudioz.fax.fme.feature.menza.utils.MenzaResult

interface MenzaRepositoryInterface {

    suspend fun fetchMenzaDetails(place: MenzaLocationType, insert: Boolean): MenzaResult

}