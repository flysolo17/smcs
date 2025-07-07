package com.jmballangca.smcsmonitoringsystem.presentation.ui.common.utils




sealed interface UiEvents {
    data class ShowErrorMessage(val message: String) : UiEvents
    data class ShowSuccessMessage(val message: String) : UiEvents
    data class Navigate<T : Any>(val route: T) : UiEvents
}
