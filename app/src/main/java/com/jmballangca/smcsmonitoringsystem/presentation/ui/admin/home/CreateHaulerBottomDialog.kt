package com.jmballangca.smcsmonitoringsystem.presentation.ui.admin.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.components.AppTextField
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.components.PrimaryButton


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateHaulerBottomDialog(
    modifier: Modifier = Modifier,
    enabled: Boolean = false,
    onSave : (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    var open by remember { mutableStateOf(false) }
    var name by remember {
        mutableStateOf("")
    }
    if (open) {
        ModalBottomSheet(
            onDismissRequest = { open = !open },
            sheetState = sheetState,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = modifier.fillMaxWidth().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Create new Hauler",
                    style = MaterialTheme.typography.titleLarge.copy(

                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(8.dp)
                )

                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Hauler Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                PrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        onSave(name)
                        name = ""
                        open = !open
                    },
                    label = "Save"
                )
            }
        }
    }
    FilledIconButton(
        shape = MaterialTheme.shapes.small,
        enabled = enabled,
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        ),
        onClick = {
            open = !open
        }
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add Hauler"
        )
    }

}