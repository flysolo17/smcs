package com.jmballangca.smcsmonitoringsystem.presentation.ui.admin.home

import com.jmballangca.smcsmonitoringsystem.data.model.Hauler
import com.jmballangca.smcsmonitoringsystem.data.model.Tenant
import com.jmballangca.smcsmonitoringsystem.data.model.User
import com.jmballangca.smcsmonitoringsystem.data.model.WasteLog


data class HomeState(
    val isLoading : Boolean = false,
    val user : User? = null,
    val wasteLogs : List<WasteLog> = emptyList(),
    val checkers : List<User> = emptyList(),
    val haulers : List<Hauler> = emptyList(),
    val tenants : List<Tenant> = emptyList(),
)