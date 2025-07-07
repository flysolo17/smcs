package com.jmballangca.smcsmonitoringsystem.presentation.ui.common.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jmballangca.smcsmonitoringsystem.data.model.User
import com.jmballangca.smcsmonitoringsystem.presentation.ui.admin.home.CreateUserBottomSheet
import com.jmballangca.smcsmonitoringsystem.presentation.ui.admin.home.HomeEvents
import com.jmballangca.smcsmonitoringsystem.ui.theme.SMCSMonitoringSystemTheme

@Composable
fun CheckersCard(
    modifier: Modifier = Modifier,
    checkers : List<User>,
    onSave: (User) -> Unit,
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
                    "Manage MRF Checkers",
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Left
                )
                CreateUserBottomSheet{
                    onSave(it)
                }
            }

            Text("Create accounts for MRF Checkers. Maximum of 3",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Gray
                ),
                textAlign = TextAlign.Left
            )
            Spacer(
                modifier = Modifier.height(8.dp)
            )
            Text("${checkers.size}/ 3 MRF Checkers", style = MaterialTheme.typography.bodySmall.copy(
                color = Color.Gray
            ))
            Spacer(
                modifier = Modifier.height(8.dp)
            )
            checkers.forEach {
                CheckersListItem(
                    user = it
                )
            }
        }

    }
}

@Preview
@Composable
private fun CheckersCardPrev() {
    SMCSMonitoringSystemTheme {
        CheckersCard(checkers = emptyList(), onSave = {})
    }

}