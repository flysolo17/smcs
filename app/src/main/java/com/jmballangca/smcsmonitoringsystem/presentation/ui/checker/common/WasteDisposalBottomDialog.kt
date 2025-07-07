package com.jmballangca.smcsmonitoringsystem.presentation.ui.checker.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jmballangca.formbuilder.FormControl
import com.jmballangca.smcsmonitoringsystem.data.model.Hauler
import com.jmballangca.smcsmonitoringsystem.data.model.Remarks
import com.jmballangca.smcsmonitoringsystem.data.model.Tenant
import com.jmballangca.smcsmonitoringsystem.data.model.WasteLog
import com.jmballangca.smcsmonitoringsystem.data.model.WasteType
import com.jmballangca.smcsmonitoringsystem.data.utils.DEFAULT_AMOUNT
import com.jmballangca.smcsmonitoringsystem.data.utils.DEFAULT_VAT
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.components.AppTextField
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.components.PrimaryButton
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.utils.formatDate
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.utils.toPhp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WasteDisposal(
    modifier: Modifier = Modifier,
    generators : List<Tenant>,
    haulers : List<Hauler>,
    onSave : (WasteLog) -> Unit
) {
    var open by remember {
        mutableStateOf(false)
    }
    val sheetState = rememberModalBottomSheetState()
    val wasteTypes = WasteType.entries
    var selectedWasteType by remember {
        mutableStateOf<WasteType?>(null)
    }
    var selectedGenerator by remember {
        mutableStateOf<Tenant?>(null)
    }
    var selectedRemark by remember {
        mutableStateOf<Remarks?>(null)

    }

    var date by remember {
        mutableLongStateOf(System.currentTimeMillis())
    }

    var selectedHauler by remember {
        mutableStateOf<Hauler?>(null)
    }

    var bags = remember {
        FormControl("")
    }
    var weight = remember {
        FormControl("")
    }
    if (open) {
        ModalBottomSheet(
            onDismissRequest = {
                open = false
            },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Waste Disposal")
                Spacer(
                    modifier = Modifier.height(12.dp)
                )
                TenantSelector(
                    modifier = Modifier.fillMaxWidth(),
                    tenants = generators,
                    selectedTenant = selectedGenerator,
                    onSelect = {
                        selectedGenerator = it
                    }
                )
                HaulerSelector(
                    modifier = Modifier.fillMaxWidth(),
                    haulers = haulers,
                    selectedHauler = selectedHauler,
                    onSelect = {
                        selectedHauler = it
                    }
                )
                WasteTypeSelector(
                    modifier = Modifier.fillMaxWidth(),
                    selectedHauler = selectedWasteType,
                    wasteTypes = wasteTypes,
                    onSelect = {
                        selectedWasteType = it
                    }
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    AppTextField(
                        modifier = Modifier.weight(1f),
                        label = "bags",
                        required = true,
                        data = bags,
                        onValueChange = {
                            bags.set(it)
                        }
                    )
                    AppTextField(
                        modifier = Modifier.weight(1f),
                        label = "Weight (kg)",
                        required = true,
                        data = weight,
                        onValueChange = {
                            weight.set(it)
                        }
                    )
                }

                RemarkSelector(
                    modifier = Modifier.fillMaxWidth(),
                    selectedRemark = selectedRemark,
                    onSelect = {
                        selectedRemark = it
                    }
                )
                PrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    label = "Dispose Waste",
                    onClick = {
                        val wasteLog = WasteLog(
                            type = com.jmballangca.smcsmonitoringsystem.data.model.LogType.DISPOSAL,
                            date = date.formatDate(),
                            wasteType = selectedWasteType ?: WasteType.RECYCLABLE,
                            generatorName = selectedGenerator?.name,
                            haulerName = selectedHauler?.name,
                            haulerId = selectedHauler?.id,
                            numberOfBags = bags.value.toIntOrNull() ?: 0,
                            weightKg = weight.value.toDoubleOrNull() ?: 0.0,
                            remarks = selectedRemark ?: Remarks.SEGREGATED,
                            createdAt = System.currentTimeMillis()
                        )
                        onSave(wasteLog)
                    }
                )
            }
        }
    }

    CheckerButton(
        modifier = modifier,
        label = "Waste Disposal",
        color = MaterialTheme.colorScheme.errorContainer,
        labelColor = MaterialTheme.colorScheme.onErrorContainer,
        icon = Icons.Default.Delete,
        onClick = {
            open = true
        }
    )


}