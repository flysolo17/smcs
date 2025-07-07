package com.jmballangca.smcsmonitoringsystem.presentation.charts

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.jmballangca.smcsmonitoringsystem.data.model.WasteLog
import com.jmballangca.smcsmonitoringsystem.ui.theme.primaryDark

@Composable
fun TotalDisposalSummary(
    modifier: Modifier = Modifier,
    wasteLog: List<WasteLog> = emptyList()
) {
    val groupedByHauler = remember(wasteLog) {
        wasteLog
            .filter { it.haulerName != null }
            .groupBy { it.haulerName!! }
    }
    val haulerBreakdown = remember(groupedByHauler) {
        groupedByHauler.mapValues { (_, logs) ->
            logs.groupBy { it.wasteType }
                .mapValues { (_, typeLogs) -> typeLogs.sumOf { it.weightKg } }
        }
    }

    OutlinedCard(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Total Disposal Summary by Hauler",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Breakdown of total waste disposal by hauler and waste type.",
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            haulerBreakdown.forEach { (hauler, wasteMap) ->
                Text(
                    text = hauler,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                wasteMap.forEach { (wasteType, totalWeight) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 12.dp, bottom = 4.dp)
                            .drawBehind {
                                val strokeWidth = 2.dp.toPx()
                                drawLine(
                                    color = primaryDark,
                                    start = Offset(0f, 0f),
                                    end = Offset(0f, size.height),
                                    strokeWidth = strokeWidth
                                )
                            }
                            .padding(start = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = wasteType.name)
                        Text(text = "${"%.2f".format(totalWeight)} kg")
                    }
                }
            }
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = "Total",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${"%.2f".format(wasteLog.sumOf { it.weightKg })} kg",
                )
            }
        }
    }
}