package com.jmballangca.smcsmonitoringsystem.presentation.charts

import android.util.Log
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jmballangca.smcsmonitoringsystem.data.model.LogType
import com.jmballangca.smcsmonitoringsystem.data.model.WasteLog
import com.jmballangca.smcsmonitoringsystem.ui.theme.gradients
import ir.ehsannarmani.compose_charts.ColumnChart
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.BarProperties
import ir.ehsannarmani.compose_charts.models.Bars
import ir.ehsannarmani.compose_charts.models.DividerProperties
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.IndicatorProperties
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.Line
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

@Composable
fun WasteLogChart(
    modifier: Modifier = Modifier,
    wasteLog: List<WasteLog>
) {
    val now = System.currentTimeMillis()
    val oneDayAgo = now - TimeUnit.HOURS.toMillis(24)


    val bucketCount = 6
    val bucketDurationMs = TimeUnit.HOURS.toMillis(4)

    val bucketRanges = remember {
        (0 until bucketCount).map { index ->
            val start = oneDayAgo + index * bucketDurationMs
            val end = start + bucketDurationMs
            start to end
        }
    }

    // Group logs into buckets
    val grouped = remember(wasteLog) {
        bucketRanges.associate { (start, end) ->
            val label = SimpleDateFormat("ha", Locale.getDefault()).let { sdf ->
                "${sdf.format(Date(end))}"
            }

            val logsInRange = wasteLog.filter {
                it.createdAt in start until end
            }
            label to logsInRange
        }
    }

    // Extract chart data
    val labels = grouped.keys.toList()
    val generatorCounts = grouped.values.map { logs ->
        logs.count { it.type == LogType.GENERATOR }.toDouble()
    }
    val disposalCounts = grouped.values.map { logs ->
        logs.count { it.type == LogType.DISPOSAL }.toDouble()
    }
    Log.d("WasteLogChart", "Generator Counts: $generatorCounts")
    Log.d("WasteLogChart", "Disposal Counts: $disposalCounts")
    Card(
        modifier.height(300.dp).padding(12.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize().padding(12.dp)
        ) {
            ColumnChart(
                modifier = Modifier.fillMaxSize().padding(horizontal = 22.dp),
                labelProperties = LabelProperties(
                    enabled = true,
                    textStyle = TextStyle.Default.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    )
                ),

                data = remember(grouped) {
                    grouped.map { (label, logs) ->
                        Bars(
                            label = label,
                            values = listOf(
                                Bars.Data(
                                    label = "Generator",
                                    value = logs.sumOf { it.weightKg },
                                    color = Brush.verticalGradient(gradients[0])
                                )
                            )
                        )
                    }
                },
                indicatorProperties = HorizontalIndicatorProperties(
                    textStyle = TextStyle.Default.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    )
                ),
                labelHelperProperties = LabelHelperProperties(
                    textStyle = TextStyle.Default.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    )
                ),
                barProperties = BarProperties(
                    cornerRadius = Bars.Data.Radius.Rectangle(topRight = 6.dp, topLeft = 6.dp),
                    spacing = 3.dp,
                    thickness = 12.dp
                ),
                gridProperties = GridProperties(
                    enabled = false
                ),
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                ),
            )
        }
    }


}