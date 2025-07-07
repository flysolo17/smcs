package com.jmballangca.smcsmonitoringsystem.presentation.ui.checker.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jmballangca.smcsmonitoringsystem.data.model.LogType
import com.jmballangca.smcsmonitoringsystem.data.model.WasteLog
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.utils.toPhp



@Composable
fun LogCard(
    modifier: Modifier = Modifier,
    log: WasteLog
) {
    val it = log
    OutlinedCard(
        modifier = modifier.padding(
            vertical = 4.dp
        )
    ) {
        ListItem(
            overlineContent = {
                Badge(
                    containerColor = if (it.type == LogType.GENERATOR) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.errorContainer,
                    contentColor = if (it.type == LogType.GENERATOR) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onErrorContainer
                ) {
                    Text(
                        modifier = Modifier.padding(
                            horizontal = 4.dp,
                            vertical = 2.dp
                        ),
                        text = it.type.name.lowercase(),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            },
            headlineContent = {
                Text(
                    text = it.generatorName.toString(),
                )
            },
            supportingContent = {
                Text(
                    text = it.date,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color.Gray
                    )
                )
            },
            trailingContent = {
                val amount = it.totalAmount.toPhp()
                Text(
                    text = "+$amount",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = if (it.type == LogType.GENERATOR) MaterialTheme.colorScheme.primary else Color.Gray
                    )
                )

            }
        )
    }
}