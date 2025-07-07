package com.jmballangca.smcsmonitoringsystem.presentation.ui.checker.home

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmballangca.smcsmonitoringsystem.data.model.Hauler
import com.jmballangca.smcsmonitoringsystem.data.model.Tenant
import com.jmballangca.smcsmonitoringsystem.data.model.User
import com.jmballangca.smcsmonitoringsystem.data.model.WasteLog
import com.jmballangca.smcsmonitoringsystem.data.repository.auth.AuthRepository
import com.jmballangca.smcsmonitoringsystem.data.repository.haulers.HaulerRepository
import com.jmballangca.smcsmonitoringsystem.data.repository.tenant.TenantRepository
import com.jmballangca.smcsmonitoringsystem.data.repository.waste.WasteLogRepository
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.utils.UiEvents
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.utils.getEndTime
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.utils.getStartTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
data class CombinedData(
    val logs: List<WasteLog>,
    val tenants: List<Tenant>,
    val user: User?,
    val haulers: List<Hauler>
)

@HiltViewModel
class CheckerHomeViewModel @Inject constructor(
    private val wasteLogRepository: WasteLogRepository,
    private val tenantRepository: TenantRepository,
    private val authRepository: AuthRepository,
    private val haulerRepository: HaulerRepository
) : ViewModel() {
    private val _state = MutableStateFlow(CheckerHomeState())
    val state = _state.asStateFlow()

    private val _uiEventFlow = Channel<UiEvents>()
    val uiEventFlow = _uiEventFlow

    init {
        events(CheckerHomeEvents.LoadRecentLogs)
    }
    fun events(e: CheckerHomeEvents) {
        when(e){
            is CheckerHomeEvents.GenerateWasteLog -> {
                generateWasteLog(e.wasteLog)
            }
            CheckerHomeEvents.LoadRecentLogs -> initializeAlldata()
        }
    }


    private fun initializeAlldata() {
        combine(
            wasteLogRepository.getRecentWasteLog(),
            tenantRepository.getAllTenants(),
            authRepository.listenToCurrentUser(),
            haulerRepository.getAllHaulers()
        ) { logs, tenants, user,haulers ->
            CombinedData(logs, tenants, user,haulers)
        }
            .onStart {
                _state.update { it.copy(isLoading = true) }

            }
            .catch { e ->
                _state.update { it.copy(isLoading = false) }
                _uiEventFlow.send(
                    UiEvents.ShowErrorMessage(e.message ?: "Something went wrong")
                )
            }
            .onEach { (logs, tenants, user,haulers) ->
                delay(1000)
                _state.update {
                    it.copy(
                        isLoading = false,
                        recentLogs = logs,
                        tenants = tenants,
                        user = user,
                        haulers = haulers
                    )
                }
            }
            .launchIn(viewModelScope)
    }


    private fun generateWasteLog(log: WasteLog) {
        val new = log.copy(
            createdBy = state.value.user?.id ?: ""
        )
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true
            )
            wasteLogRepository.addWasteLog(new).onSuccess {
                _state.value = _state.value.copy(
                    isLoading = false
                )
                _uiEventFlow.send(UiEvents.ShowSuccessMessage("Waste log generated successfully"))
            }.onFailure {
                _state.value = _state.value.copy(
                    isLoading = false
                )
                _uiEventFlow.send(UiEvents.ShowErrorMessage(it.message ?: "Something went wrong"))
            }
        }
    }
}