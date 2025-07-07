package com.jmballangca.smcsmonitoringsystem.presentation.ui.admin.logs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import com.jmballangca.smcsmonitoringsystem.presentation.ui.checker.common.LogCard
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import com.jmballangca.smcsmonitoringsystem.R
import com.jmballangca.smcsmonitoringsystem.data.utils.WasteLogHeaderData
import com.jmballangca.smcsmonitoringsystem.data.utils.WasteLogPdfGenerator
import com.jmballangca.smcsmonitoringsystem.data.utils.toBitmap
import com.jmballangca.smcsmonitoringsystem.presentation.ui.checker.common.DateSelector
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.ErrorMessage
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.LoadingIndicator
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.components.DateRangePicker
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.utils.UiEvents
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.utils.formatDate
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.utils.showToast


@Composable
fun LogScreen(
    viewModel: LogViewModel = hiltViewModel()
) {

    val wasteLogs = viewModel.wasteLogsFlow.collectAsLazyPagingItems()
    val state by viewModel.state.collectAsState()
    val events = viewModel::events
    val context = LocalContext.current
    val uiEvents = viewModel.uiEventFlow
    LaunchedEffect(Unit) {
       uiEvents.collect {
           when(it) {
               is UiEvents.Navigate<*> -> {

               }
               is UiEvents.ShowErrorMessage -> {
                   context.showToast(it.message)
               }
               is UiEvents.ShowSuccessMessage -> {
                   context.showToast(it.message)
               }
           }
       }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxSize().padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween

            ) {
                Text("Logs", style = MaterialTheme.typography.titleMedium)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    DateRangePicker(
                        value = Pair(state.startDate, state.endDate),
                        onValueChange = {
                            events(LogEvents.OnDateSelected(it.first, it.second))
                        }
                    )
                }
            }
        }
        item {
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                enabled = !state.isGeneratingReport,
                onClick = {
                    val imageRes : Int = R.drawable.logo
                    events(LogEvents.GenerateReport(imageRes.toBitmap(context)))
                }
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (state.isGeneratingReport) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(14.dp)
                        )
                    } else {
                        Text("Generate Report")
                    }
                }

            }
        }
        items(wasteLogs.itemCount) { index ->
            val log = wasteLogs[index]
            if (log != null) {
                LogCard(log = log)
            }
        }
        wasteLogs.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item {
                        LoadingIndicator()
                    }
                }
                loadState.append is LoadState.Loading -> {
                    item {
                        LoadingIndicator()
                    }
                }
                loadState.refresh is LoadState.Error -> {
                    val e = (loadState.refresh as LoadState.Error).error
                    item {
                        ErrorMessage(message = e.localizedMessage ?: "Unknown error")
                    }
                }
                loadState.append is LoadState.Error -> {
                    val e = (loadState.append as LoadState.Error).error
                    item {
                        ErrorMessage(message = e.localizedMessage ?: "Unknown error")
                    }
                }
            }
        }
    }
}
