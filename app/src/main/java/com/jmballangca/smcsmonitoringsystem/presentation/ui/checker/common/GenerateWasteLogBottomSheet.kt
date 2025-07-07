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
import com.jmballangca.smcsmonitoringsystem.data.model.Remarks
import com.jmballangca.smcsmonitoringsystem.data.model.Tenant
import com.jmballangca.smcsmonitoringsystem.data.model.WasteLog
import com.jmballangca.smcsmonitoringsystem.data.model.WasteType
import com.jmballangca.smcsmonitoringsystem.data.utils.DEFAULT_AMOUNT
import com.jmballangca.smcsmonitoringsystem.data.utils.DEFAULT_VAT
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.components.PrimaryButton
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.utils.formatDate
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.utils.toPhp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenerateWasteLog(
    modifier: Modifier = Modifier,
    generators : List<Tenant>,
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
                Text("Waste Generator")
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
                DateSelector(
                    modifier = Modifier.fillMaxWidth(),
                    selectedDate = date,
                    onDateSelected = {
                        date = it
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
                RemarkSelector(
                    modifier = Modifier.fillMaxWidth(),
                    selectedRemark = selectedRemark,
                    onSelect = {
                        selectedRemark = it
                    }
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Amount", style = MaterialTheme.typography.titleMedium.copy(
                  
                    ))
                    Text(
                        DEFAULT_AMOUNT.toPhp(), style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.Gray
                    ))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("VAT", style = MaterialTheme.typography.titleMedium.copy(
                    ))
                    Text(
                        DEFAULT_VAT.toPhp(), style = MaterialTheme.typography.titleMedium.copy(color = Color.Gray))
                }
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth().padding(
                        vertical = 12.dp
                    ),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Total Amount", style = MaterialTheme.typography.titleMedium)
                    Text(
                        (DEFAULT_AMOUNT + DEFAULT_VAT).toPhp(),
                        style = MaterialTheme.typography.titleMedium.copy(color = Color.Gray)
                    )
                }

                PrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    label = "Generate",
                    onClick = {
                        val wasteLog = WasteLog(
                            type = com.jmballangca.smcsmonitoringsystem.data.model.LogType.GENERATOR,
                            date = date.formatDate(),
                            wasteType = selectedWasteType ?: WasteType.RECYCLABLE,
                            generatorName = selectedGenerator?.name,
                            numberOfBags = 0,
                            weightKg = 0.0,
                            remarks = selectedRemark ?: Remarks.SEGREGATED,
                            totalAmount = DEFAULT_AMOUNT + DEFAULT_VAT,
                            createdAt = System.currentTimeMillis()
                        )
                        onSave(wasteLog)
                    }
                )
            }
        }
    }

    CheckerButton(
        label = "Waste Generator",
        modifier = modifier,
        icon = Icons.Default.Add,
        onClick = {
            open = true
        }
    )

}