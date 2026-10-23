package com.tstudioz.fax.fme.feature.iksica

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.tstudioz.fax.fme.R
import com.tstudioz.fax.fme.feature.home.compose.noRippleClickable
import com.tstudioz.fax.fme.feature.iksica.compose.BottomSheetIksica
import com.tstudioz.fax.fme.feature.iksica.compose.CardIksicaPopupContent
import com.tstudioz.fax.fme.feature.iksica.compose.ElevatedCardIksica
import com.tstudioz.fax.fme.feature.iksica.compose.IksicaItem
import com.tstudioz.fax.fme.feature.iksica.compose.IksicaReceiptState
import com.tstudioz.fax.fme.feature.iksica.compose.NestedSheetState
import com.tstudioz.fax.fme.feature.iksica.compose.PopupBox
import com.tstudioz.fax.fme.feature.iksica.compose.rememberNestedSheetState
import com.tstudioz.fax.fme.feature.iksica.models.IksicaData
import com.tstudioz.fax.fme.feature.iksica.models.Receipt
import com.tstudioz.fax.fme.theme.contentColors
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(
    InternalCoroutinesApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class
)
@Composable
fun IksicaScreen(iksicaViewModel: IksicaViewModel, innerPaddingValues: PaddingValues) {

    val lifecycleState by LocalLifecycleOwner.current.lifecycle.currentStateFlow.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val listState = rememberLazyListState()
    val nestedSheetState = rememberNestedSheetState(composableHeight = 999, sheetOffset = 999)

    val receiptSelected = iksicaViewModel.receiptSelected.observeAsState().value
    val iksicaData = iksicaViewModel.iksicaData.observeAsState().value
    val isRefreshing = iksicaViewModel.isRefreshing.collectAsState().value

    val showPopup = remember { mutableStateOf(false) }

    LaunchedEffect(lifecycleState) {
        if (lifecycleState == Lifecycle.State.RESUMED) iksicaViewModel.getReceipts()
    }

    val snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
    val error = iksicaViewModel.error

    LaunchedEffect(Unit) {
        error.collectLatest {
            it?.let { message ->
                snackbarHostState.showSnackbar(message)
            }
        }
    }

    DisposableEffect(lifecycleState) {
        onDispose {
            if (lifecycleState == Lifecycle.State.CREATED) {
                coroutineScope.launch {
                    listState.scrollToItem(0)
                }
            }
        }
    }
    BottomSheetScaffold(
        sheetPeekHeight = 0.dp,
        modifier = Modifier.padding(innerPaddingValues),
        scaffoldState = scaffoldState,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        sheetContent = {
            if (receiptSelected is IksicaReceiptState.Success)
                BottomSheetIksica(receiptSelected.data) { iksicaViewModel.hideReceiptDetails() }
        },
    ) {
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { iksicaViewModel.getReceipts() },
        ) {
            Column{
                TopBarIksica()
                if (iksicaData != null) {
                    PopulatedIksicaView(
                        iksicaData,
                        listState,
                        nestedSheetState,
                        onCardClick = { showPopup.value = true },
                        onItemClick = { iksicaViewModel.getReceiptDetails(it) }
                    )
                } else {
                    EmptyIksicaView()
                }
            }
        }
    }

    PopupBox(showPopup = showPopup.value, onClickOutside = { showPopup.value = !showPopup.value }) {
        iksicaData?.studentData?.let { CardIksicaPopupContent(it) }
    }
}

@OptIn(InternalCoroutinesApi::class)
@Composable
fun EmptyIksicaView() {
    LazyColumn(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        item {
            EmptyIksicaContent(stringResource(id = R.string.iksica_no_data))
        }
    }
}

@OptIn(InternalCoroutinesApi::class)
@Composable
fun PopulatedIksicaView(
    model: IksicaData,
    listState: LazyListState,
    nestedSheetState: NestedSheetState,
    onCardClick: () -> Unit,
    onItemClick: (Receipt) -> Unit
) {
    val sheetOffset = nestedSheetState.sheetOffset
    val sheetTopPadding = nestedSheetState.sheetTopPadding
    val composableHeight = nestedSheetState.composableHeight

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                if (listState.firstVisibleItemIndex == 0) {
                    sheetOffset.intValue =
                        (sheetOffset.intValue + delta).coerceIn(
                            sheetTopPadding,
                            composableHeight.intValue.toFloat()
                        )
                            .toInt()
                }
                return Offset(
                    0f,
                    if (composableHeight.intValue > sheetOffset.intValue && sheetOffset.intValue > sheetTopPadding) delta else 0f
                )
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ) =
                Offset(0f, if (sheetOffset.intValue == 0) available.y else 0f)
        }
    }

    Box(modifier = Modifier.nestedScroll(nestedScrollConnection)) {
        Column(Modifier.onGloballyPositioned {
            if (!nestedSheetState.set) {
                nestedSheetState.set = true
                composableHeight.intValue = it.size.height
                sheetOffset.intValue = it.size.height
            }
        }) {
            Box(Modifier.fillMaxWidth()) {
                ElevatedCardIksica(
                    model.studentData.nameSurname,
                    model.studentData.cardNumber,
                    model.studentData.balance
                ) {
                    onCardClick()
                }
            }
        }
        Column(
            modifier = Modifier
                .offset { IntOffset(0, sheetOffset.intValue) }
                .clip(RoundedCornerShape(30.dp, 30.dp, 0.dp, 0.dp))
                .background(MaterialTheme.colorScheme.surface)
                .noRippleClickable {}
        ) {
            val receipts = model.receipts
            LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                item {
                    TransactionsText()
                }
                if (receipts.isNullOrEmpty()) {
                    item { EmptyIksicaContent(stringResource(id = R.string.iksica_no_receipts)) }
                } else {
                    items(receipts) {
                        IksicaItem(it) { onItemClick(it) }
                    }
                }
            }
        }
    }
}

@OptIn(InternalCoroutinesApi::class)
@Composable
fun TopBarIksica() {
    Row(
        modifier = Modifier
            .background(Color.Transparent)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(id = R.string.tab_iksica),
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.contentColors.primary,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun TransactionsText() {
    Text(
        text = stringResource(id = R.string.transactions),
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        color = MaterialTheme.contentColors.primary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp, 30.dp, 16.dp, 24.dp)
    )
}

@Composable
fun EmptyIksicaContent(text: String) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp, 100.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = text,
            color = MaterialTheme.contentColors.secondary
        )
    }
}