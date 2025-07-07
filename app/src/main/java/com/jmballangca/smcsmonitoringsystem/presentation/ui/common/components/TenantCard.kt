package com.jmballangca.smcsmonitoringsystem.presentation.ui.common.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jmballangca.smcsmonitoringsystem.data.model.Tenant
import com.jmballangca.smcsmonitoringsystem.presentation.ui.admin.home.CreateHaulerBottomDialog
import com.jmballangca.smcsmonitoringsystem.presentation.ui.admin.home.CreateTenantBottomDialog


@Composable
fun TenantCard(
    modifier: Modifier = Modifier,
    tenants: List<Tenant>,
    onSave : (Tenant) -> Unit,
) {
    OutlinedCard(
        modifier = modifier.padding(8.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),

                ) {
                Text(
                    "Manage Waste Generators (Tenants)",
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Left
                )
                CreateTenantBottomDialog {
                    onSave(it)
                }
            }

            Text("Add or remove tenants waste generators.Maximum of 500",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Gray
                ),
                textAlign = TextAlign.Left
            )
            Spacer(
                modifier = Modifier.height(8.dp)
            )
            Text("${tenants.size}/ 500 Waste Generators", style = MaterialTheme.typography.bodySmall.copy(
                color = Color.Gray
            ))
            Spacer(
                modifier = Modifier.height(8.dp)
            )
            tenants.forEach {
                TenantListItem(
                    tenant = it,
                    onClick = {}
                ) { }
            }
        }
    }
}