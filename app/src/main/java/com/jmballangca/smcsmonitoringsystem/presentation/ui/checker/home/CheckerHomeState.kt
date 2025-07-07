package com.jmballangca.smcsmonitoringsystem.presentation.ui.checker.home

import com.jmballangca.smcsmonitoringsystem.data.model.Hauler
import com.jmballangca.smcsmonitoringsystem.data.model.Tenant
import com.jmballangca.smcsmonitoringsystem.data.model.User
import com.jmballangca.smcsmonitoringsystem.data.model.WasteLog


data class CheckerHomeState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val tenants : List<Tenant> = emptyList(),
    val haulers : List<Hauler> = emptyList(),
    val recentLogs : List<WasteLog> = emptyList()
)