package com.jmballangca.smcsmonitoringsystem.presentation.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmballangca.smcsmonitoringsystem.data.model.UserRole
import com.jmballangca.smcsmonitoringsystem.data.repository.auth.AuthRepository
import com.jmballangca.smcsmonitoringsystem.presentation.navigation.ADMIN
import com.jmballangca.smcsmonitoringsystem.presentation.navigation.CHECKER

import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.utils.UiEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()


    private val _uiEventFlow = MutableSharedFlow<UiEvents>()
    val uiEventFlow: SharedFlow<UiEvents> = _uiEventFlow

    fun events(
        e: LoginEvents
    ) {
        when(e) {
            LoginEvents.Login -> login()
            is LoginEvents.PasswordChanged -> {
                val form = _state.value.loginForm
                form.set("password", e.password)

            }
            is LoginEvents.UsernameChanged -> {
                val form = _state.value.loginForm
                form.set("username", e.username)
            }

            LoginEvents.GetCurrentUser -> getCurrentUser()
            LoginEvents.TogglePasswordVisibility -> _state.value = _state.value.copy(
                isPasswordVisible = !_state.value.isPasswordVisible
            )
        }
    }

    private fun getCurrentUser() {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true
            )
            authRepository.getCurrentUser().onSuccess {
                _state.value = _state.value.copy(
                    isLoading = false,
                    user = it
                )
                val role = it?.role
                if (it != null) {
                    _uiEventFlow.emit(UiEvents.ShowSuccessMessage("Logged in successfully"))
                }
                if (role == UserRole.ADMIN) {
                    _uiEventFlow.emit(UiEvents.Navigate(ADMIN))
                }
                if (role == UserRole.MRF_CHECKER) {
                    _uiEventFlow.emit(UiEvents.Navigate(CHECKER))
                }
            }.onFailure {
                _state.value = _state.value.copy(
                    isLoading = false,
                    user = null
                )
            }
        }
    }

    private fun login() {
        val loginForm = _state.value.loginForm
        val username = loginForm.get("username")?.value ?: ""
        val password = loginForm.get("password")?.value ?: ""
        if (username.isEmpty() && password.isEmpty()) {
            UiEvents.ShowErrorMessage("username and password cannot be empty")
            return
        }
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true
            )
            authRepository.login(username, password).onSuccess {
                delay(1000)
                _state.value = _state.value.copy(
                    isLoading = false
                )
                _uiEventFlow.emit(UiEvents.ShowSuccessMessage("Logged in successfully"))
                val role = it?.role
                if (role == UserRole.ADMIN) {
                    _uiEventFlow.emit(UiEvents.Navigate(ADMIN))
                }
                if (role == UserRole.MRF_CHECKER) {
                    _uiEventFlow.emit(UiEvents.Navigate(CHECKER))
                }
            }.onFailure {
                _state.value = _state.value.copy(
                    isLoading = false
                )
                _uiEventFlow.emit(UiEvents.ShowErrorMessage("Invalid username or password"))

            }
        }
    }


}