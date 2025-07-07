package com.jmballangca.smcsmonitoringsystem.presentation.ui.admin.hauler

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.jmballangca.smcsmonitoringsystem.data.model.Hauler
import com.jmballangca.smcsmonitoringsystem.presentation.ui.admin.checkers.CheckerEvents
import com.jmballangca.smcsmonitoringsystem.presentation.ui.admin.home.CreateHaulerBottomDialog
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.AppCollapsingToolbar
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.AppContainer
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.components.CheckersListItem
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.components.HaulerListItem
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.utils.UiEvents
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.utils.showToast
import java.util.UUID


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HaulersScreen(modifier: Modifier = Modifier,
                  navController: NavHostController,
                  viewModel: HaulerViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val context = LocalContext.current
    val state = viewModel.state.collectAsStateWithLifecycle()
    val event = viewModel::events
    val eventFlow = viewModel.uiEventFlow

    LaunchedEffect(Unit) {
        eventFlow.collect {
            when(it) {
                is UiEvents.ShowErrorMessage -> {
                    context.showToast(it.message)
                }
                is UiEvents.ShowSuccessMessage -> {
                    context.showToast(it.message)
                }
                is UiEvents.Navigate<*> -> {

                }
            }
        }
    }
    AppContainer(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppCollapsingToolbar(
                scrollBehavior = scrollBehavior,
                title = { Text("Haulers")},
                name = "Manage Haulers",
                description = "Add or remove haulers. Maximum of 5",
                values = "${state.value.haulers.size} / 5 Haulers",
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    CreateHaulerBottomDialog(
                        enabled = state.value.haulers.size < 5,
                        onSave = {
                            val hauler = Hauler(
                                id = UUID.randomUUID().toString(),
                                name = it
                            )
                            event(HaulersEvent.OnCreateHauler(hauler))
                        }
                    )
                },

            )
        }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth().padding(14.dp
            ),
            contentPadding = it
        ) {
            items(
                state.value.haulers,
                key = { it.id }
            ) {
                HaulerListItem(
                    hauler = it,
                    onDelete = {
                        event(HaulersEvent.OnDelete(it.id))
                    }
                )
            }

        }
    }
}