package com.jmballangca.smcsmonitoringsystem.presentation.ui.checker.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Badge
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.jmballangca.smcsmonitoringsystem.data.model.Hauler
import com.jmballangca.smcsmonitoringsystem.data.model.LogType
import com.jmballangca.smcsmonitoringsystem.data.model.Tenant
import com.jmballangca.smcsmonitoringsystem.data.model.User
import com.jmballangca.smcsmonitoringsystem.data.model.WasteLog
import com.jmballangca.smcsmonitoringsystem.data.model.WasteType
import com.jmballangca.smcsmonitoringsystem.presentation.navigation.HAULER
import com.jmballangca.smcsmonitoringsystem.presentation.navigation.LOGS
import com.jmballangca.smcsmonitoringsystem.presentation.navigation.MRF_CHECKERS
import com.jmballangca.smcsmonitoringsystem.presentation.navigation.TENANT
import com.jmballangca.smcsmonitoringsystem.presentation.ui.checker.common.CheckerButton
import com.jmballangca.smcsmonitoringsystem.presentation.ui.checker.common.GenerateWasteLog
import com.jmballangca.smcsmonitoringsystem.presentation.ui.checker.common.LogCard
import com.jmballangca.smcsmonitoringsystem.presentation.ui.checker.common.WasteDisposal
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.FeatureCard
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.utils.UiEvents
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.utils.showToast
import com.jmballangca.smcsmonitoringsystem.ui.theme.gradients

import kotlinx.coroutines.flow.receiveAsFlow


@Composable
fun CheckerHome(
    modifier: Modifier = Modifier,
    viewModel: CheckerHomeViewModel = hiltViewModel<CheckerHomeViewModel>(),
    navHostController: NavHostController
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val uiEvent = viewModel.uiEventFlow.receiveAsFlow()
    val tenants = state.value.tenants
    val logs = state.value.recentLogs
    val isLoading = state.value.isLoading
    val context = LocalContext.current
    val haulers = state.value.haulers
    LaunchedEffect(Unit) {
        uiEvent.collect {
            when (it) {
                is UiEvents.Navigate<*> -> {
                    navHostController.navigate(it.route)
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
    CheckerHome(
        modifier = modifier,
        tenants = tenants,
        haulers = haulers,
        user = state.value.user,
        navigateToLogs = {
            navHostController.navigate(LOGS)
        },
        logs = logs,
        isLoading = isLoading,
        events = viewModel::events
    )
}
@Composable
fun CheckerHome(
    modifier: Modifier = Modifier,
    tenants : List<Tenant>,
    logs : List<WasteLog>,
    haulers : List<Hauler>,
    user : User?,
    isLoading : Boolean,
    navigateToLogs : () -> Unit,
    events : (CheckerHomeEvents) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        if (isLoading) {
            item {
                LinearProgressIndicator(
                    modifier = modifier.fillMaxWidth().padding(
                        bottom = 12.dp
                    )
                )
            }
        }
        item {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .background(brush = Brush.linearGradient(colors = gradients[3]), shape = MaterialTheme.shapes.medium)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "SMCS - MRF Monitoring System",
                    modifier= Modifier.align(Alignment.CenterHorizontally).padding(12.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium.copy(color = Color.White),
                )

                Column(
                    modifier = modifier.fillMaxWidth().background(
                        color = MaterialTheme.colorScheme.background,
                        shape = MaterialTheme.shapes.extraLarge
                    ).padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("${user?.name}", style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    ))
                    Text("${user?.role}", style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Light,
                        color = MaterialTheme.colorScheme.outline,

                        ))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    WasteDisposal(
                        modifier = modifier.weight(1f),
                        generators =tenants,
                        haulers = haulers,
                        onSave = {
                            events(CheckerHomeEvents.GenerateWasteLog(it))
                        }
                    )
                    GenerateWasteLog(
                        modifier = modifier.weight(1f),
                        generators =tenants,
                        onSave = {
                            events(CheckerHomeEvents.GenerateWasteLog(it))
                        }
                    )
                }

            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = 12.dp
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Recent",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                TextButton(
                    onClick = {
                        navigateToLogs()
                    }
                ) {
                    Text("View All",
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
        }
        items(
            items = logs,
            key = { it.id }
        ) {
            LogCard(
                modifier = Modifier.fillMaxWidth(),
                log = it
            )
        }

    }
}