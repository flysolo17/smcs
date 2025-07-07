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
import com.jmballangca.smcsmonitoringsystem.data.model.Hauler
import com.jmballangca.smcsmonitoringsystem.presentation.ui.admin.home.CreateHaulerBottomDialog
import com.jmballangca.smcsmonitoringsystem.presentation.ui.admin.home.CreateUserBottomSheet


@Composable
fun HaulersCard(
    modifier: Modifier = Modifier,
    haulers : List<Hauler>,
    onSave: (String) -> Unit,
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
                    "Manage Haulers",
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Left
                )
                CreateHaulerBottomDialog {
                    onSave(it)
                }
            }

            Text("Add or remove haulers. Maximum of 5",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Gray
                ),
                textAlign = TextAlign.Left
            )
            Spacer(
                modifier = Modifier.height(8.dp)
            )
            Text("${haulers.size}/ 5 Haulers", style = MaterialTheme.typography.bodySmall.copy(
                color = Color.Gray
            ))
            Spacer(
                modifier = Modifier.height(8.dp)
            )
            haulers.forEach {
                HaulerListItem(
                    hauler = it,
                    onDelete = {}
                )
            }
        }
    }
}