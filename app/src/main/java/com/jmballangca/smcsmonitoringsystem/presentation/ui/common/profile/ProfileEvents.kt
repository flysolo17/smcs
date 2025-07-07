package com.jmballangca.smcsmonitoringsystem.presentation.ui.common.profile




sealed interface ProfileEvents {
    object GetCurrentUser : ProfileEvents
    object Logout : ProfileEvents

}