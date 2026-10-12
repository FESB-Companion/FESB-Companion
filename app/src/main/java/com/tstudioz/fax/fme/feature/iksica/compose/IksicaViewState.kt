package com.tstudioz.fax.fme.feature.iksica.compose

import com.tstudioz.fax.fme.feature.iksica.models.Receipt

sealed class IksicaReceiptState {
    data object None : IksicaReceiptState()
    data object Fetching : IksicaReceiptState()
    data class Success(val data: Receipt) : IksicaReceiptState()
    data class Error(val message: String) : IksicaReceiptState()
}