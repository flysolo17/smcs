package com.jmballangca.smcsmonitoringsystem.presentation.ui.common.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.utils.formatDate

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*
fun Long.shortDate(): String {
    val sdf = SimpleDateFormat("MMM dd", Locale.getDefault())
    return sdf.format(Date(this))
}

@Composable
fun DateRangePicker(
    modifier: Modifier = Modifier,
    value: Pair<Long, Long>,
    onValueChange: (Pair<Long, Long>) -> Unit,
) {
    var open by remember { mutableStateOf(false) }

    if (open) {
        DateRangePickerModal(
            onDateRangeSelected = { selectedRange ->
                val start = selectedRange.first ?: value.first
                val end = selectedRange.second ?: value.second
                onValueChange(Pair(start, end))
            },
            onDismiss = { open = false }
        )
    }

    TextButton(
        modifier = modifier,
        onClick = { open = true }
    ) {
        Text("${value.first.shortDate()} - ${value.second.shortDate()}")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerModal(
    onDateRangeSelected: (Pair<Long?, Long?>) -> Unit,
    onDismiss: () -> Unit
) {
    val dateRangePickerState = rememberDateRangePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onDateRangeSelected(
                        Pair(
                            dateRangePickerState.selectedStartDateMillis,
                            dateRangePickerState.selectedEndDateMillis
                        )
                    )
                    onDismiss()
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DateRangePicker(
            state = dateRangePickerState,
            title = { Text("Select date range") },
            showModeToggle = false,
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .padding(16.dp)
        )
    }
}

