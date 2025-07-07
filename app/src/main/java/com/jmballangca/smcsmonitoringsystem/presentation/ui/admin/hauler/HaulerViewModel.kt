package com.jmballangca.smcsmonitoringsystem.presentation.ui.admin.hauler

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmballangca.smcsmonitoringsystem.data.model.Hauler
import com.jmballangca.smcsmonitoringsystem.data.repository.haulers.HaulerRepository
import com.jmballangca.smcsmonitoringsystem.presentation.ui.admin.checkers.CheckerEvents
import com.jmballangca.smcsmonitoringsystem.presentation.ui.admin.checkers.CheckerState
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.utils.UiEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HaulerViewModel @Inject constructor(
    private val haulerRepository: HaulerRepository
): ViewModel() {
    private val _state = MutableStateFlow(HaulersState())
    val state = _state.asStateFlow()
    private val _uiEventFlow : Channel<UiEvents> = Channel()
    val uiEventFlow = _uiEventFlow.receiveAsFlow()

    val _haulers = haulerRepository.getAllHaulers().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )
    init {

        combine(
            _state,_haulers
        ) { state, haulers ->
            state.copy(
                haulers = haulers
            )
        }.onEach {
            _state.value = it
        }.launchIn(viewModelScope)
    }
    fun events(e : HaulersEvent) {
        when (e) {
            is HaulersEvent.OnCreateHauler -> createHauler(e.name)

            is HaulersEvent.OnDelete -> delete(e.id)
        }
    }

    private fun delete(string: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true
            )
            haulerRepository.deleteHaulers(string)
                .onSuccess {
                    _state.value = _state.value.copy(isLoading = false)
                    _uiEventFlow.send(UiEvents.ShowSuccessMessage("Hauler deleted successfully"))
                }
                .onFailure {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _uiEventFlow.send(UiEvents.ShowErrorMessage("Failed to delete Hauler"))
                }
        }
    }

    private fun createHauler(hauler: Hauler) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true
            )
            haulerRepository.createHauler(hauler).onSuccess {
                _state.value = _state.value.copy(
                    isLoading = false
                )
                _uiEventFlow.send(UiEvents.ShowSuccessMessage("Hauler created successfully"))
            }.onFailure {
                _state.value = _state.value.copy(
                 isLoading = false
                )
                _uiEventFlow.send(UiEvents.ShowErrorMessage("Failed to create Hauler"))
            }
        }
    }
}