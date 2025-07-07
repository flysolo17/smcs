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
import com.jmballangca.smcsmonitoringsystem.data.model.Hauler
import com.jmballangca.smcsmonitoringsystem.data.model.WasteType
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.components.AppTextField
import kotlin.collections.forEach
import kotlin.enums.EnumEntries


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WasteTypeSelector(
    modifier: Modifier = Modifier,
    selectedHauler: WasteType?,
    wasteTypes : EnumEntries<WasteType> = WasteType.entries,
    onSelect: (WasteType) -> Unit
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
            label = "Waste Type",
            data = FormControl(selectedHauler?.name ?: "--Select--"),
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
            wasteTypes.forEach { type ->
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
