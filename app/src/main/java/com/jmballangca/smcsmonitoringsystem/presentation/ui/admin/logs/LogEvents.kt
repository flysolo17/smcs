package com.jmballangca.smcsmonitoringsystem.presentation.ui.admin.logs

import android.graphics.Bitmap
import java.time.LocalDate


sealed interface LogEvents {
    data object Logout : LogEvents
    data class OnDateSelected(val startDate: Long,val endDate : Long) : LogEvents
    data class GenerateReport(
        val logo : Bitmap?
    ): LogEvents
}