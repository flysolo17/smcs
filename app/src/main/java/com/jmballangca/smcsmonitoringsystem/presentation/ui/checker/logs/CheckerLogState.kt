package com.jmballangca.smcsmonitoringsystem.presentation.ui.checker.logs

import com.jmballangca.smcsmonitoringsystem.data.model.User
import com.jmballangca.smcsmonitoringsystem.data.model.WasteLog
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.utils.START_OF_YEAR


data class CheckerLogState(
    val isLoading: Boolean = false,
    val isGeneratingReport : Boolean = false,
    val logs: List<WasteLog> = emptyList(),
    val pageSize: Int = 10,
    val user: User? = null,
    val startDate: Long = START_OF_YEAR,
    val endDate: Long = System.currentTimeMillis()

)