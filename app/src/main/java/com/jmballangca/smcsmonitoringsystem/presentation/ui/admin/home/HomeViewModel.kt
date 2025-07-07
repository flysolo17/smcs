package com.jmballangca.smcsmonitoringsystem.presentation.ui.admin.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmballangca.smcsmonitoringsystem.data.model.Hauler
import com.jmballangca.smcsmonitoringsystem.data.model.Tenant
import com.jmballangca.smcsmonitoringsystem.data.model.User
import com.jmballangca.smcsmonitoringsystem.data.repository.auth.AuthRepository
import com.jmballangca.smcsmonitoringsystem.data.repository.haulers.HaulerRepository
import com.jmballangca.smcsmonitoringsystem.data.repository.tenant.TenantRepository
import com.jmballangca.smcsmonitoringsystem.data.repository.waste.WasteLogRepository
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.utils.UiEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val haulerRepository: HaulerRepository,
    private val tenantRepository: TenantRepository,
    private val wasteLogRepository: WasteLogRepository
)  : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val checkersFlow = authRepository.getAllMRFChecker()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
    private val _uiEventFlow : Channel<UiEvents> = Channel()
    val uiEventFlow = _uiEventFlow.receiveAsFlow()

    private val _haulers = haulerRepository.getAllHaulers().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )
    private val _tenants = tenantRepository.getAllTenants().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )
    private val _user = authRepository.listenToCurrentUser().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )
    private val now = System.currentTimeMillis()
    private val last24Hours = now - TimeUnit.HOURS.toMillis(24)

    private val wasteLogsFlow = wasteLogRepository.getAllWasteLog(
        startAt = last24Hours,
        endAt = now,
        limit = 1000
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        combine(checkersFlow, _haulers, _tenants, _user, wasteLogsFlow) { checkers, haulers, tenants, user, logs ->
            _state.value = _state.value.copy(
                checkers = checkers,
                haulers = haulers,
                tenants = tenants,
                user = user,
                wasteLogs = logs
            )
        }.launchIn(viewModelScope,)
    }


fun events(e : HomeEvents) {
        when(e) {
            else -> {}
        }
    }





}