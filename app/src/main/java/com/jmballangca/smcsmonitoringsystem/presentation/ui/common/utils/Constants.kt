package com.jmballangca.smcsmonitoringsystem.presentation.ui.common.utils

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

val START_OF_YEAR: Long = Calendar.getInstance().apply {
    set(Calendar.YEAR, 2025)
    set(Calendar.MONTH, Calendar.JANUARY)
    set(Calendar.DAY_OF_MONTH, 1)
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
}.timeInMillis


fun Double?.toPhp(): String {
    if (this == null) {
        return "₱ 0.00"
    }
    return "₱ %.2f".format(this)
}

fun Context.showToast(
    message : String,
    duration : Int = Toast.LENGTH_SHORT
) {
    Toast.makeText(this, message, duration).show()
}


fun Long.formatDate(): String {
    val sdf = java.text.SimpleDateFormat("MMM/dd/yyyy", Locale.getDefault())
    return sdf.format(Date(this))
}

fun Long.getStartTime(): Long {
    val calendar = Calendar.getInstance().apply {
        timeInMillis = this@getStartTime
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    return calendar.timeInMillis
}

fun Long.getEndTime(): Long {
    val calendar = Calendar.getInstance().apply {
        timeInMillis = this@getEndTime
        set(Calendar.HOUR_OF_DAY, 23)
        set(Calendar.MINUTE, 59)
        set(Calendar.SECOND, 59)
        set(Calendar.MILLISECOND, 999)
    }
    return calendar.timeInMillis
}