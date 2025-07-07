package com.jmballangca.smcsmonitoringsystem.presentation.ui.common.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmballangca.smcsmonitoringsystem.data.repository.auth.AuthRepository
import com.jmballangca.smcsmonitoringsystem.presentation.navigation.LOGIN
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.utils.UiEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()
    private val _uiEventFlow = Channel<UiEvents>(Channel.BUFFERED)
    val uiEventFlow = _uiEventFlow.receiveAsFlow()

    private val userFlow = authRepository.listenToCurrentUser()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    init {
        viewModelScope.launch {
            combine(_state, userFlow) { state, user ->
                state.copy(user = user)
            }.collect { newState ->
                _state.value = newState
            }
        }
    }

    fun events(event: ProfileEvents) {
        when (event) {
            is ProfileEvents.GetCurrentUser -> {
                // Optional: Trigger specific user-related actions if needed
            }
            is ProfileEvents.Logout -> {
                logout()
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            authRepository.logout()
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
                    _uiEventFlow.send(UiEvents.ShowSuccessMessage("Logged out successfully"))
                    _uiEventFlow.send(UiEvents.Navigate(LOGIN))
                }
                .onFailure {
                    _state.update { it.copy(isLoading = false) }
                    _uiEventFlow.send(UiEvents.ShowErrorMessage(it.message ?: "Something went wrong"))
                }
        }
    }
}
