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
import com.jmballangca.smcsmonitoringsystem.data.model.Tenant
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.components.AppTextField


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TenantSelector(
    modifier: Modifier = Modifier,
    tenants : List<Tenant>,
    selectedTenant : Tenant?,
    onSelect : (Tenant) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        AppTextField(
            label = "Select Hauler",
            data = FormControl(selectedTenant?.name ?: "--Select Tenant--"),
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
            tenants.forEach { unit ->
                DropdownMenuItem(
                    text = { Text(unit.name) },
                    onClick = {
                        onSelect(unit)
                        expanded = false
                    }
                )
            }
        }
    }

}