package com.jmballangca.smcsmonitoringsystem.presentation.ui.admin.checkers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmballangca.smcsmonitoringsystem.data.model.User
import com.jmballangca.smcsmonitoringsystem.data.repository.auth.AuthRepository
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.utils.UiEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.forEach

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CheckerViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {
    private val _state = MutableStateFlow(CheckerState())
    val state = _state.asStateFlow()
    private val _uiEventFlow : Channel<UiEvents> = Channel()
    val uiEventFlow = _uiEventFlow.receiveAsFlow()

    private val checkers = authRepository.getAllMRFChecker()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        combine(_state, checkers) { currentState, checkersList ->
            currentState.copy(checkers = checkersList)
        }.onEach { new ->
            _state.value = new
        }.launchIn(
            viewModelScope
        )
    }


    fun events(e : CheckerEvents) {
        when(e) {
            is CheckerEvents.OnCreateCheckers -> createCheckers(e.user)
            is CheckerEvents.OnDelete -> deleteCheckers(e.id)
        }
    }

    private fun deleteCheckers(string: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true
            )
            authRepository.deleteMRFChecker(string).onSuccess {
                _state.value = _state.value.copy(
                    isLoading = false
                )
                _uiEventFlow.send(UiEvents.ShowSuccessMessage("MRF Checker deleted successfully"))
                }.onFailure {
                _state.value = _state.value.copy(
                    checkers = emptyList()
                )
                _uiEventFlow.send(UiEvents.ShowErrorMessage("Failed to delete MRF Checker"))
            }
        }
    }

    private fun createCheckers(user: User) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true
            )
            authRepository.createMRFChecker(user).onSuccess {
                _state.value = _state.value.copy(
                    isLoading = false
                )
                _uiEventFlow.send(UiEvents.ShowSuccessMessage("MRF Checker created successfully"))

            }.onFailure {
                _state.value = _state.value.copy(
                    checkers = emptyList()
                )
                _uiEventFlow.send(UiEvents.ShowErrorMessage("Failed to create MRF Checker"))
            }
        }
    }
}