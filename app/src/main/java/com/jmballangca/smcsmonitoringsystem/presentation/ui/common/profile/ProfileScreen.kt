package com.jmballangca.smcsmonitoringsystem.presentation.ui.common.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.jmballangca.smcsmonitoringsystem.data.model.User
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.components.PrimaryButton
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.utils.UiEvents
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.utils.showToast
import com.jmballangca.smcsmonitoringsystem.ui.theme.gradients


@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel<ProfileViewModel>(),
    navController: NavHostController,
    mainNavController: NavHostController
) {

    val state = viewModel.state.collectAsStateWithLifecycle()
    val events = viewModel::events
    val uiEventFlow = viewModel.uiEventFlow
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        uiEventFlow.collect {
            when(it) {
                is UiEvents.Navigate<*> -> {
                    mainNavController.navigate(it.route) {
                        popUpTo(0) { inclusive = true } // Clear entire backstack
                        launchSingleTop = true
                    }
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
    ProfileScreen(
        user = state.value.user,
        isLoading = state.value.isLoading,
        events = events
    )

}
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    user: User? = null,
    events: (ProfileEvents) -> Unit,
    ) {
    Column(
        modifier = modifier.fillMaxSize().background(
            brush = Brush.verticalGradient(
                colors = gradients[3]
            )
        ).padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = modifier.fillMaxWidth().height(200.dp)
        ) {
            Column(
                modifier = modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("${user?.name}", style = MaterialTheme.typography.headlineLarge.copy(
                    color = MaterialTheme.colorScheme.onBackground
                ))
                Text("${user?.role}", style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.outline))
                Spacer(
                    modifier = Modifier.height(16.dp)
                )
                PrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    label = "Logout",
                    enabled = !isLoading,
                    loading = isLoading,
                    onClick = {
                        events(ProfileEvents.Logout)
                    }
                )
            }
        }
    }
}