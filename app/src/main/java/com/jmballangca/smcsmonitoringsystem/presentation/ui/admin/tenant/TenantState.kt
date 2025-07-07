package com.jmballangca.smcsmonitoringsystem.presentation.ui.admin.tenant

import com.jmballangca.smcsmonitoringsystem.data.model.Tenant


data class TenantState(
    val isLoading : Boolean = false,
    val tenants : List<Tenant> = emptyList(),
)