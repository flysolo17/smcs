package com.jmballangca.smcsmonitoringsystem.presentation.ui.admin.checkers

import com.jmballangca.smcsmonitoringsystem.data.model.User


data class CheckerState(
    val checkers : List<User> = emptyList(),
    val isLoading : Boolean = false
)