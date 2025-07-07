package com.jmballangca.smcsmonitoringsystem.presentation.ui.auth.login

sealed interface LoginEvents {
    data class UsernameChanged(val username: String) : LoginEvents
    data class PasswordChanged(val password: String) : LoginEvents
    object Login : LoginEvents

    object GetCurrentUser : LoginEvents
    object TogglePasswordVisibility : LoginEvents
}