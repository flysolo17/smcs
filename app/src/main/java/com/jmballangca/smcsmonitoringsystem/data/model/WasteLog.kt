package com.jmballangca.smcsmonitoringsystem.data.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import java.util.Calendar

data class WasteLog(
    val id: String = "",
    val type: LogType = LogType.GENERATOR,
    val date: String = "",
    val wasteType: WasteType = WasteType.RECYCLABLE,
    val generatorName: String? = null,
    val numberOfBags: Int = 0,
    val weightKg: Double = 0.0,
    val haulerId: String? = null,
    val haulerName: String? = null,
    val remarks: Remarks = Remarks.SEGREGATED,
    val totalAmount: Double? = null,
    val createdBy: String = "",
    val createdAt: Long = System.currentTimeMillis()
)






enum class Remarks {
    SEGREGATED,
    NOT_SEGREGATED,
}
enum class LogType {
    GENERATOR,
    DISPOSAL
}

enum class WasteType {
    RECYCLABLE,
    DISPOSABLE,
    COMPOSTABLE,
    USED_OIL,
    MIXED,
    OTHER_HAZARDOUS
}
