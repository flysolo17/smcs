package com.jmballangca.smcsmonitoringsystem.presentation.ui.admin.tenant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmballangca.smcsmonitoringsystem.data.model.Tenant
import com.jmballangca.smcsmonitoringsystem.data.repository.tenant.TenantRepository
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
class TenantViewModel @Inject constructor(
    private val tenantRepository: TenantRepository
): ViewModel() {
    private val _state = MutableStateFlow(TenantState())
    val state = _state.asStateFlow()
    private val _uiEventFlow = Channel<UiEvents>()
    val uiEventFlow = _uiEventFlow.receiveAsFlow()
    val _tenants = tenantRepository.getAllTenants().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )
    init {
        combine(_state, _tenants) { state, tenants ->
            state.copy(
                tenants = tenants
            )
        }.onEach {
            _state.value = it
        }.launchIn(viewModelScope)
    }
    fun events(e : TenantEvents) {
        when(e) {
            is TenantEvents.OnCreateTenant -> createTenant(e.tenant)
            is TenantEvents.OnDelete -> deleteTenant(e.id)
        }
    }

    private fun createTenant(tenant: Tenant) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true
            )
            tenantRepository.createTenant(tenant).onSuccess {
                _state.value = _state.value.copy(
                    isLoading = false
                )
                _uiEventFlow.send(UiEvents.ShowSuccessMessage("Tenant created successfully"))
            }.onFailure {
                _state.value = _state.value.copy(
                    isLoading = false
                )
                _uiEventFlow.send(UiEvents.ShowErrorMessage("Failed to create Tenant"))
            }
        }
    }

    private fun deleteTenant(string: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true
            )
            tenantRepository.deleteTenant(string).onSuccess {
                _state.value = _state.value.copy(
                    isLoading = false
                )
                _uiEventFlow.send(UiEvents.ShowSuccessMessage("Tenant deleted successfully"))
            }.onFailure {
                _state.value = _state.value.copy(
                    isLoading = false
                )
                _uiEventFlow.send(UiEvents.ShowErrorMessage("Failed to delete Tenant"))
            }
        }
    }
}