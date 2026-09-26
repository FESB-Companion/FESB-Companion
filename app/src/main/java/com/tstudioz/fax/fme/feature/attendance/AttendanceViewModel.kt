package com.tstudioz.fax.fme.feature.attendance

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.tstudioz.fax.fme.R
import com.tstudioz.fax.fme.feature.attendance.models.AttendanceEntry
import com.tstudioz.fax.fme.feature.attendance.repository.AttendanceRepositoryInterface
import com.tstudioz.fax.fme.feature.attendance.utils.ShownSemester
import com.tstudioz.fax.fme.networking.InternetConnectionObserver
import com.tstudioz.fax.fme.networking.NetworkServiceResult
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class AttendanceViewModel(
    private val repository: AttendanceRepositoryInterface,
    private val application: Application
) : ViewModel() {

    val internetAvailable: LiveData<Boolean> = InternetConnectionObserver.get()

    private var _attendanceListFull: MutableLiveData<List<List<AttendanceEntry>>> =
        MutableLiveData(emptyList())
    val attendanceListFull: LiveData<List<List<AttendanceEntry>>> = _attendanceListFull

    private val attendanceFirstSem =
        _attendanceListFull.map { list -> list.filter { it.firstOrNull()?.semester == 1 } }
    private val attendanceSecondSem =
        _attendanceListFull.map { list -> list.filter { it.firstOrNull()?.semester == 2 } }

    private val _shownSemester: MutableLiveData<ShownSemester?> = MutableLiveData(null)
    val shownSemester: LiveData<ShownSemester?> = _shownSemester

    private val _attendance: LiveData<List<List<AttendanceEntry>>> = _shownSemester.switchMap {
        when (it) {
            ShownSemester.FIRST -> attendanceFirstSem
            ShownSemester.SECOND -> attendanceSecondSem
            null -> _attendanceListFull
        }
    }

    val attendance: LiveData<List<List<AttendanceEntry>>> = _attendance

    private val _error = MutableSharedFlow<String?>()
    val error: SharedFlow<String?> = _error

    private suspend fun showSnackbar(message: String) {
        _error.emit(message)
    }

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private val handler = CoroutineExceptionHandler { _, exception ->
        Log.e("Error attendance", exception.toString())
        viewModelScope.launch(Dispatchers.Main) {
            showSnackbar(application.getString(R.string.general_error))
        }
    }

    init {
        loadFromDb()
        fetchAttendance()
    }

    fun fetchAttendance() {
        if (internetAvailable.value == false) return
        viewModelScope.launch(context = Dispatchers.IO + handler) {
            _isRefreshing.emit(true)

            when (val attendance = repository.fetchAttendance()) {
                is NetworkServiceResult.AttendanceParseResult.Success -> {
                    _attendanceListFull.postValue(attendance.data)
                }

                is NetworkServiceResult.AttendanceParseResult.Failure -> {
                    showSnackbar(application.getString(R.string.general_error))
                }
            }

            _isRefreshing.emit(false)
        }
    }

    private fun loadFromDb() {
        viewModelScope.launch(context = Dispatchers.IO + handler) {
            _attendanceListFull.postValue(repository.readAttendance())
        }
    }

    fun showSemester(semester: ShownSemester) {
        _shownSemester.value = if (_shownSemester.value == semester) null else semester
    }
}