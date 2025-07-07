package com.jmballangca.smcsmonitoringsystem.presentation.ui.admin.checkers

import com.jmballangca.smcsmonitoringsystem.data.model.User
import com.jmballangca.smcsmonitoringsystem.presentation.ui.admin.home.HomeEvents


interface CheckerEvents  {
    data class OnCreateCheckers(
        val user: User
    ) : CheckerEvents

    data class OnDelete(
        val id : String
    ) : CheckerEvents
}