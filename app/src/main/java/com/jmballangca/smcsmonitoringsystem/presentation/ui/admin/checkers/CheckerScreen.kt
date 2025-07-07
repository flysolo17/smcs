package com.jmballangca.smcsmonitoringsystem.presentation.ui.admin.checkers

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.jmballangca.smcsmonitoringsystem.presentation.ui.admin.home.CreateUserBottomSheet
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.AppCollapsingToolbar
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.AppContainer
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.components.CheckersListItem
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.utils.UiEvents
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.utils.showToast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckerScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: CheckerViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val event = viewModel::events
    val eventFlow = viewModel.uiEventFlow
    val context = LocalContext.current
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
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())


    AppContainer(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppCollapsingToolbar(
                scrollBehavior = scrollBehavior,
                title = { Text("MRF Checkers")},
                name = "Manage MRF Checkers",
                description = "Create accounts for MRF Checkers. Maximum of 3",
                values = "${
                    state.value.checkers.size
                } / 3 MRF Checkers",
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
                    CreateUserBottomSheet(
                        enabled = state.value.checkers.size < 3,
                        onSave = {
                            event(CheckerEvents.OnCreateCheckers(it))
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
                state.value.checkers,
                key = { it.id }
            ) {
                CheckersListItem(
                    user = it,
                    onDelete = {
                        event(CheckerEvents.OnDelete(it.id))
                    }
                )
            }

        }
    }
}