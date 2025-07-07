package com.jmballangca.smcsmonitoringsystem.presentation.ui.checker.common

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.jmballangca.formbuilder.FormControl
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.components.AppTextField
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.utils.formatDate
import java.util.Calendar

@Composable
fun DateSelector(
    modifier: Modifier = Modifier,
    selectedDate: Long = System.currentTimeMillis(),
    onDateSelected: (Long) -> Unit
) {
    val context = LocalContext.current
    var dateText by remember { mutableStateOf(selectedDate.formatDate()) }

    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            calendar.set(year, month, dayOfMonth)
            val timeInMillis = calendar.timeInMillis
            dateText = timeInMillis.formatDate()
            onDateSelected(timeInMillis)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    AppTextField(
        label = "Select Date",
        data = FormControl(dateText),
        required = true,
        readOnly = true,
        onValueChange = {},
        modifier = modifier
            .fillMaxWidth()
            .clickable { datePickerDialog.show() },
        trailingIcon = {
            IconButton(onClick = { datePickerDialog.show() }) {
                Icon(imageVector = Icons.Default.DateRange, contentDescription = "Pick Date")
            }
        }
    )

}