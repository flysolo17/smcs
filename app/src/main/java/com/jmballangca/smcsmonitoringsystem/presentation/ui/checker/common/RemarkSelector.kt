package com.jmballangca.smcsmonitoringsystem.presentation.ui.checker.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.jmballangca.formbuilder.FormControl
import com.jmballangca.smcsmonitoringsystem.data.model.Remarks
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.components.AppTextField
import kotlin.enums.EnumEntries


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemarkSelector(
    modifier: Modifier = Modifier,
    remarks : EnumEntries<Remarks> = Remarks.entries,
    selectedRemark : Remarks?,
    onSelect : (Remarks) -> Unit
    ) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded, // hook this up later with state
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        AppTextField(
            label = "Select Remarks",
            data = FormControl(selectedRemark?.name ?: "--Select Remark--"),
            required = true,
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            onValueChange = {},
            modifier = modifier
                .fillMaxWidth()
                .menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            remarks.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type.name) },
                    onClick = {
                        onSelect(type)
                        expanded = false
                    }
                )
            }
        }
    }

}