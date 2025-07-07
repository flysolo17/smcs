package com.jmballangca.smcsmonitoringsystem.presentation.charts

import android.R
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CheckboxDefaults.colors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jmballangca.smcsmonitoringsystem.data.model.WasteLog
import com.jmballangca.smcsmonitoringsystem.data.model.WasteType
import ir.ehsannarmani.compose_charts.PieChart
import ir.ehsannarmani.compose_charts.models.Pie
import kotlin.enums.EnumEntries


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DisposalPieChart(
    modifier: Modifier = Modifier,
    disposal: List<WasteLog>
) {
    val groupByHaulerName  = disposal.groupBy { it.haulerName }

    val pieColors = listOf(
        Color(0xFF23AF92),
        Color(0xFFFF9800),
        Color(0xFF3F51B5),
        Color(0xFFE91E63),
        Color(0xFF9C27B0),
    )


    var data by remember(groupByHaulerName) {
        mutableStateOf(
            groupByHaulerName.entries.mapIndexed { index, entry ->
                Pie(
                    label = entry.key,
                    data = entry.value.size.toDouble(),
                    color = pieColors[index],
                )
            }
        )
    }

    Card(
        modifier = modifier.padding(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
           verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PieChart(
                modifier = Modifier.size(200.dp),
                data = data,
                onPieClick = {
                    println("${it.label} Clicked")
                    val pieIndex = data.indexOf(it)
                    data = data.mapIndexed { mapIndex, pie -> pie.copy(selected = pieIndex == mapIndex) }
                },
                selectedScale = 1.2f,
                scaleAnimEnterSpec = spring<Float>(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                ),
                colorAnimEnterSpec = tween(300),
                colorAnimExitSpec = tween(300),
                scaleAnimExitSpec = tween(300),
                spaceDegreeAnimExitSpec = tween(300),
                style = Pie.Style.Fill
            )

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                maxItemsInEachRow = 3
            ) {
                groupByHaulerName.entries.mapIndexed { index, entry ->
                    CirclerWithColor(
                        color = pieColors[index],
                        name = entry.key ?: "Unknown"
                    )
                }
            }
        }

    }
}

@Composable
fun CirclerWithColor(
    modifier: Modifier = Modifier,
    color: Color = Color.Blue,
    name : String,
) {
    Row(
        modifier = modifier.padding(2.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(
                    color = color,
                    shape = CircleShape
                )
        )
        Text(name)
    }

}