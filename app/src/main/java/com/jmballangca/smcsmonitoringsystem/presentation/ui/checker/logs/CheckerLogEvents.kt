package com.jmballangca.smcsmonitoringsystem.presentation.ui.checker.logs

import android.graphics.Bitmap
import com.jmballangca.smcsmonitoringsystem.data.model.User
import com.jmballangca.smcsmonitoringsystem.presentation.ui.admin.checkers.CheckerEvents
import com.jmballangca.smcsmonitoringsystem.presentation.ui.admin.logs.LogEvents

sealed interface CheckerLogEvents {
    data object Logout : CheckerLogEvents
    data class OnDateSelected(val startDate: Long,val endDate : Long) : CheckerLogEvents
    data class GenerateReport(
        val logo : Bitmap?
    ): CheckerLogEvents
}