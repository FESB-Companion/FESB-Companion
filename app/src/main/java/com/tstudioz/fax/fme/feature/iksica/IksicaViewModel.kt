package com.tstudioz.fax.fme.feature.iksica

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tstudioz.fax.fme.R
import com.tstudioz.fax.fme.feature.iksica.compose.IksicaReceiptState
import com.tstudioz.fax.fme.feature.iksica.models.IksicaData
import com.tstudioz.fax.fme.feature.iksica.models.IksicaResult
import com.tstudioz.fax.fme.feature.iksica.models.Receipt
import com.tstudioz.fax.fme.feature.iksica.repository.IksicaRepositoryInterface
import com.tstudioz.fax.fme.networking.InternetConnectionObserver
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@InternalCoroutinesApi
class IksicaViewModel(
    private val repository: IksicaRepositoryInterface,
    private val application: Application
) : ViewModel() {

    private val _error = MutableSharedFlow<String?>()
    val error: SharedFlow<String?> = _error

    private suspend fun showSnackbar(message: String) {
        _error.emit(message)
    }

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    val internetAvailable: LiveData<Boolean> = InternetConnectionObserver.get()

    private val _iksicaData = MutableLiveData<IksicaData?>(null)
    val iksicaData: LiveData<IksicaData?> = _iksicaData

    private val _receiptSelected = MutableLiveData<IksicaReceiptState>(IksicaReceiptState.None)
    val receiptSelected: LiveData<IksicaReceiptState> = _receiptSelected

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("Iksica", throwable.message.toString())
        viewModelScope.launch(Dispatchers.Main) {
            _isRefreshing.emit(false)
            showSnackbar(application.getString(R.string.error_general_iksica))
        }
    }

    init {
        loadReceiptsFromCache()
    }

    private fun loadReceiptsFromCache() {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val model = repository.getCache() ?: return@launch

            _iksicaData.postValue(model)
        }
    }

    fun getReceipts() {
        if (internetAvailable.value == false) return
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            _isRefreshing.emit(true)
            when (val result = repository.getCardDataAndReceipts()) {
                is IksicaResult.CardAndReceiptsResult.Success -> {
                    val model = result.data

                    _iksicaData.postValue(model)
                }

                is IksicaResult.CardAndReceiptsResult.Failure -> {
                    showSnackbar(
                        application.getString(R.string.error_fetching_receipts_iksica),
                    )
                }
            }
            _isRefreshing.emit(false)
        }
    }

    fun getReceiptDetails(receipt: Receipt?) {
        if (receipt == null) {
            hideReceiptDetails()
            return
        }
        if (internetAvailable.value == false) return
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            _receiptSelected.postValue(IksicaReceiptState.Fetching)
            when (val details = repository.getReceipt(receipt.url)) {
                is IksicaResult.ReceiptResult.Success -> {
                    _receiptSelected.postValue(
                        IksicaReceiptState.Success(
                            receipt.copy(
                                receiptDetails = details.data
                            )
                        )
                    )
                }

                is IksicaResult.ReceiptResult.Failure -> {
                    _receiptSelected.postValue(IksicaReceiptState.Error(details.throwable.message.toString()))
                    showSnackbar(
                        application.getString(R.string.error_receipt_details_iksica),
                    )
                }
            }
        }
    }

    fun hideReceiptDetails() {
        _receiptSelected.postValue(IksicaReceiptState.None)
    }
}