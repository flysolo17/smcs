package com.jmballangca.smcsmonitoringsystem.presentation.ui.admin.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jmballangca.smcsmonitoringsystem.data.model.Tenant
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.components.PrimaryButton


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTenantBottomDialog(
    modifier: Modifier = Modifier,
    onSave : (Tenant) -> Unit) {
    val sheetState = rememberModalBottomSheetState()
    var isOpen by remember { mutableStateOf(false) }
    var name by remember {
        mutableStateOf("")
    }
    if (isOpen) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { isOpen = false },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = modifier.fillMaxWidth().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Create Waste Generator",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(8.dp),
                    textAlign = TextAlign.Center
                )
                OutlinedTextField(
                    value = name,
                    shape = MaterialTheme.shapes.medium,
                    singleLine = true,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth().padding(
                        bottom = 8.dp
                    )
                )
                PrimaryButton(
                    label = "Save",

                    onClick = {
                        isOpen = false
                        onSave(
                            Tenant(
                                name = name
                            ))
                    }
                )
            }
        }
    }

    FilledIconButton(
        shape = MaterialTheme.shapes.small,
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        ),
        onClick = {
            isOpen = true
        }
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add"
        )
    }

}