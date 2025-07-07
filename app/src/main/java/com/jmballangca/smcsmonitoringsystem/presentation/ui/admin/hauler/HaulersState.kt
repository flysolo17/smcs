package com.jmballangca.smcsmonitoringsystem.presentation.ui.admin.hauler

import com.jmballangca.smcsmonitoringsystem.data.model.Hauler
import com.jmballangca.smcsmonitoringsystem.data.model.User


data class HaulersState(
    val haulers: List<Hauler> = emptyList(),
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null

)