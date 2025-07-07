package com.jmballangca.smcsmonitoringsystem.presentation.ui.admin.home

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FireTruck
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
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
import com.jmballangca.smcsmonitoringsystem.presentation.charts.DisposalPieChart
import com.jmballangca.smcsmonitoringsystem.presentation.charts.WasteLogChart
import com.jmballangca.smcsmonitoringsystem.presentation.navigation.HAULER
import com.jmballangca.smcsmonitoringsystem.presentation.navigation.LOGS
import com.jmballangca.smcsmonitoringsystem.presentation.navigation.MRF_CHECKERS
import com.jmballangca.smcsmonitoringsystem.presentation.navigation.TENANT
import com.jmballangca.smcsmonitoringsystem.presentation.ui.checker.common.LogCard
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.FeatureCard
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.components.CheckersCard
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.components.HaulersCard
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.components.TenantCard
import com.jmballangca.smcsmonitoringsystem.ui.theme.gradients
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.Line

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel<HomeViewModel>(),
    navController: NavHostController,
    mainNavController: NavHostController
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val events = viewModel::events

    HomeScreen(
        checkers = state.value.checkers,
        haulers = state.value.haulers,
        user = state.value.user,
        events = events,
        tenants = state.value.tenants,
        logs = state.value.wasteLogs,
        navigateToLogs = {
            navController.navigate(LOGS)
        },
        navigate = {
            mainNavController.navigate(it)
        }
    )

}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    user: User?,
    checkers: List<User>,
    haulers: List<Hauler>,
    tenants : List<Tenant>,
    logs : List<WasteLog>,
    events: (HomeEvents) -> Unit,
    navigateToLogs : () ->Unit,
    navigate : (T : Any) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ){
        item {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(12.dp)
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
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    FeatureCard(
                        modifier = Modifier.weight(1f),
                        title = "MRF's",
                        value = "${checkers.size} / 3"
                    ) {
                        navigate(MRF_CHECKERS)
                    }
                    FeatureCard(
                        modifier = Modifier.weight(1f),
                        title = "Haulers",
                        value = "${haulers.size} / 5"
                    ) {        navigate(HAULER) }
                    FeatureCard(
                        modifier = Modifier.weight(1f),
                        title = "Tenants",
                        value = "${tenants.size} / 500"
                    ) { navigate(TENANT) }
                }
            }
        }

        item {
            Column(
                modifier = Modifier.fillMaxWidth().padding(
                    horizontal = 12.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    "Waste Logs (Last 24 hrs)",
                    style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onBackground),
                )
                Text("Total weight (kg) of waste generated by tenants",style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.outline
                ), modifier = modifier.padding(
                    top = 4.dp,
                ).width(250.dp))
            }
        }
        item {
            WasteLogChart(
                wasteLog = logs
            )
        }
        item {
            Column(
                modifier = Modifier.fillMaxWidth().padding(
                    horizontal = 12.dp
                ),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    "Waste Disposal (Last 24 hrs)",
                    style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onBackground),
                )
                Text("Percentage of total waste disposed by hauler.",style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.outline
                ), modifier = modifier.padding(
                    top = 4.dp,
                ).width(250.dp))
            }
        }
        item {
            DisposalPieChart(
                disposal = logs.filter { it.type == LogType.DISPOSAL }
            )
        }
        item {
            Row(
                modifier = modifier.fillMaxWidth().padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Recent",

                    style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onBackground),
                )
                TextButton(
                    onClick = {
                        navigateToLogs()
                    }
                ) {
                    Text(
                        "View All",
                        style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.primary)
                    )
                }
            }

        }
        items(
            logs.sortedByDescending { it.createdAt }.take(10),
            key = {it.id}
        ) {
            LogCard(
                modifier = Modifier.padding(horizontal = 12.dp),
                log = it
            )
        }

    }



}