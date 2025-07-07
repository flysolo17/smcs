package com.jmballangca.smcsmonitoringsystem.presentation.ui.checker.home

import com.jmballangca.smcsmonitoringsystem.data.model.WasteLog


sealed interface CheckerHomeEvents {
    data object LoadRecentLogs : CheckerHomeEvents
    data class GenerateWasteLog(
        val wasteLog: WasteLog
    ) : CheckerHomeEvents

}