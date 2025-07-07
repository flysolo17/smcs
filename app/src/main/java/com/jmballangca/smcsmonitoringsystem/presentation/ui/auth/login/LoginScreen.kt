package com.jmballangca.smcsmonitoringsystem.presentation.ui.auth.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController

import com.jmballangca.formbuilder.FormControl
import com.jmballangca.smcsmonitoringsystem.presentation.navigation.ADMIN
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.components.AppTextField
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.components.PrimaryButton
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.utils.UiEvents
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.utils.showToast
import com.jmballangca.smcsmonitoringsystem.ui.theme.SMCSMonitoringSystemTheme

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val events = viewModel::events
    val context = LocalContext.current
    val loginForm = state.loginForm

    val username = remember(loginForm) { loginForm.get("username") }
    val password = remember(loginForm) { loginForm.get("password") }

    val hasError by remember(username, password) {
        derivedStateOf {
            val usernameHasError = username?.dirty == true && username.valid == false
            val passwordHasError = password?.dirty == true && password.valid == false
            usernameHasError || passwordHasError
        }
    }

    LaunchedEffect(Unit) {
        events(LoginEvents.GetCurrentUser)
    }

    LaunchedEffect(Unit) {
        viewModel.uiEventFlow.collect { event ->
            when (event) {
                is UiEvents.Navigate<*> -> {
                    navController.navigate(event.route)
                }
                is UiEvents.ShowErrorMessage -> {
                    context.showToast(event.message)
                }
                is UiEvents.ShowSuccessMessage -> {
                    context.showToast(event.message)
                }
            }
        }
    }

    LoginScreen(
        username = username,
        password = password,
        isPasswordVisible = state.isPasswordVisible,
        invalidForm = hasError,
        events = events,
        isLoggingIn = state.isLoading
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    username : FormControl ?,
    password : FormControl?,
    isPasswordVisible : Boolean,
    invalidForm : Boolean = false,
    isLoggingIn : Boolean = false,
    events: (LoginEvents) -> Unit,
) {

    Scaffold(
        topBar = { TopAppBar(
            title = { Text(text = "") }
        ) }
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()

                .padding(it)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Filled.Recycling,
                contentDescription = "Login",
                tint = MaterialTheme.colorScheme.primary,
                modifier = modifier.size(42.dp)
            )
            Spacer(
                modifier = Modifier.size(16.dp)
            )
            Text("SM City Consolacion - MRF Monitoring System",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 16.dp,
                    vertical = 8.dp
                )
            )
            Text("Enter your credential to access your account",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color= Color.Gray
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp,
                    vertical = 8.dp
                )
                )

            AppTextField(
                label = "Username",
                data = username,
                onValueChange = { events(LoginEvents.UsernameChanged(it)) },
            )

            AppTextField(
                label = "Password",
                data = password,
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                trailingIcon = {
                    val visibilityIcon = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { events.invoke(LoginEvents.TogglePasswordVisibility) }) {
                        Icon(
                            imageVector = visibilityIcon,
                            contentDescription = if (isPasswordVisible) "Hide password" else "Show password"
                        )
                    }
                },
                onValueChange = { events(LoginEvents.PasswordChanged(it)) }
            )

            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                label = "Login",
                onClick = { events(LoginEvents.Login) },
                enabled = !isLoggingIn && !invalidForm,
                loading = isLoggingIn
            )
        }
    }
}

@Preview(
    showSystemUi = true,
    showBackground = true
)
@Composable
private fun LoginScreenPrev() {
    SMCSMonitoringSystemTheme {
        LoginScreen(
            isPasswordVisible = false,
            username = FormControl(""),
            password = FormControl(""),
            events = {}
        )
    }
}