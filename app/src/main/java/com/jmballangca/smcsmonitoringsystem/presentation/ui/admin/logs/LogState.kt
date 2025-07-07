package com.jmballangca.smcsmonitoringsystem.presentation.ui.admin.logs

import com.jmballangca.smcsmonitoringsystem.data.model.User
import com.jmballangca.smcsmonitoringsystem.data.model.WasteLog
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.utils.START_OF_YEAR

import java.util.Calendar


data class LogState(
    val isLoading: Boolean = false,
    val isGeneratingReport : Boolean = false,
    val logs: List<WasteLog> = emptyList(),
    val pageSize: Int = 50,
    val user: User? = null,
    val startDate: Long = START_OF_YEAR,
    val endDate: Long = System.currentTimeMillis()
)
