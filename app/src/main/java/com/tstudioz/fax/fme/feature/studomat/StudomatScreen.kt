package com.tstudioz.fax.fme.feature.studomat

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.currentStateAsState
import com.tstudioz.fax.fme.R
import com.tstudioz.fax.fme.feature.studomat.compose.EmptyStudomatView
import com.tstudioz.fax.fme.feature.studomat.compose.StudomatContent
import com.tstudioz.fax.fme.feature.studomat.compose.WebViewScreen
import com.tstudioz.fax.fme.networking.cookies.MonsterCookieJar
import com.tstudioz.fax.fme.theme.studomatBlue
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StudomatScreen(studomatViewModel: StudomatViewModel, innerPaddingValues: PaddingValues) {

    val studomatData = studomatViewModel.studomatData.observeAsState().value

    val snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
    val error = studomatViewModel.error
    val isRefreshing = studomatViewModel.isRefreshing.observeAsState().value
    val openedWebview = remember { mutableStateOf(false) }
    val cookieJar = koinInject<MonsterCookieJar>()

    val lifecycleState = LocalLifecycleOwner.current.lifecycle.currentStateAsState().value
    LaunchedEffect(lifecycleState) {
        if (lifecycleState == Lifecycle.State.RESUMED) {
            studomatViewModel.getStudomatData()
        }
    }
    LaunchedEffect(Unit) {
        error.collectLatest {
            it?.let { message ->
                snackbarHostState.showSnackbar(message)
            }
        }
    }

    Scaffold(
        Modifier
            .background(Brush.verticalGradient(listOf(studomatBlue, Color.Transparent)))
            .padding(innerPaddingValues),
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            if (!openedWebview.value){
                Text(
                    text = stringResource(id = R.string.tab_studomat),
                    style = MaterialTheme.typography.displayMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing == true,
            onRefresh = {
                studomatViewModel.getStudomatData(pulldownTriggered = true)
            }
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (openedWebview.value) {
                    BackHandler { openedWebview.value = false }
                    WebViewScreen(cookieJar)
                    return@PullToRefreshBox
                }
                LazyColumn {
                    item {
                        if (!studomatData.isNullOrEmpty()) {
                            StudomatContent(studomatData, onClick = { openedWebview.value = true })
                        } else {
                            Column(Modifier.fillParentMaxSize()) { EmptyStudomatView() }
                        }
                    }
                }
            }
        }
    }
}