package com.jmballangca.smcsmonitoringsystem.presentation.ui.common.profile

import com.jmballangca.smcsmonitoringsystem.data.model.User


data class ProfileState(
    val isLoading : Boolean = false,
    val user: User? = null,
)