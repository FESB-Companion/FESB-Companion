package com.tstudioz.fax.fme.feature.attendance

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.tstudioz.fax.fme.R
import com.tstudioz.fax.fme.feature.attendance.compose.AttendanceItem
import com.tstudioz.fax.fme.feature.attendance.utils.ShownSemester
import com.tstudioz.fax.fme.theme.AppTheme
import com.tstudioz.fax.fme.theme.contentColors
import com.tstudioz.fax.fme.theme.theme_dark_primaryContainer
import com.tstudioz.fax.fme.theme.theme_dark_secondaryContainer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest

@OptIn(InternalCoroutinesApi::class, ExperimentalCoroutinesApi::class)
@Composable
fun AttendanceScreen(attendanceViewModel: AttendanceViewModel, innerPaddingValues: PaddingValues) {

    val items = attendanceViewModel.attendanceListFull.observeAsState().value ?: emptyList()

    val lifecycleState by LocalLifecycleOwner.current.lifecycle.currentStateFlow.collectAsState()
    val snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
    val error = attendanceViewModel.error
    LaunchedEffect(Unit) {
        error.collectLatest {
            it?.let { message ->
                snackbarHostState.showSnackbar(message)
            }
        }
    }
    LaunchedEffect(lifecycleState) {
        when (lifecycleState) {
            Lifecycle.State.RESUMED -> {
                attendanceViewModel.fetchAttendance()
            }

            else -> {}
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        contentWindowInsets = WindowInsets(0.dp),
        modifier = Modifier.padding(innerPaddingValues),
        topBar = {
            Text(
                text = stringResource(id = R.string.tab_attendance),
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.displayMedium,
            )
        }
    ) {
        PullToRefreshBox(
            isRefreshing = attendanceViewModel.isRefreshing.collectAsState().value,
            onRefresh = {
                attendanceViewModel.fetchAttendance()
            }
        ) {
            LazyColumn(
                Modifier.padding(it)
            ) {
                item {
                    if (items.isNotEmpty()) {
                        CreateAttendanceListView(attendanceViewModel)
                    } else {
                        Column(Modifier.fillParentMaxSize()) { EmptyView() }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyView() {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(stringResource(id = R.string.no_data))
    }
}

@OptIn(InternalCoroutinesApi::class, ExperimentalCoroutinesApi::class)
@Composable
fun CreateAttendanceListView(
    attendanceViewModel: AttendanceViewModel
) {
    val list by attendanceViewModel.attendance.observeAsState(emptyList())
    val shownSemester by attendanceViewModel.shownSemester.observeAsState()

    Column(Modifier) {
        Row(
            Modifier.padding(horizontal = 32.dp)
        ) {
            FilterButton(
                selected = shownSemester == ShownSemester.FIRST,
                text = stringResource(id = R.string.first_semester),
                onClick = { attendanceViewModel.showSemester(ShownSemester.FIRST) })
            FilterButton(
                selected = shownSemester == ShownSemester.SECOND,
                text = stringResource(id = R.string.second_semester),
                onClick = { attendanceViewModel.showSemester(ShownSemester.SECOND) })
        }

        list.forEach { item ->
            AttendanceItem(item)
        }
    }
}

@Composable
fun FilterButton(
    selected: Boolean, text: String, onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .background(color = if (selected) theme_dark_secondaryContainer else theme_dark_primaryContainer),
    ) {
        Text(
            text = text,
            color = MaterialTheme.contentColors.primary,
            modifier = Modifier.padding(12.dp, 6.dp),
            fontSize = 14.sp
        )
    }
    Spacer(modifier = Modifier.padding(8.dp))
}

@Preview
@Composable
fun FilterButtonPreview() {
    AppTheme {
        FilterButton(selected = true, text = "First Semester", onClick = {})
    }
}