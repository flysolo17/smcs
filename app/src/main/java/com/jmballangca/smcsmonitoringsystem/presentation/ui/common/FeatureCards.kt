package com.jmballangca.smcsmonitoringsystem.presentation.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jmballangca.smcsmonitoringsystem.ui.theme.SMCSMonitoringSystemTheme


@Composable
fun FeatureCard(
    modifier: Modifier = Modifier,
    title : String,
    value : String,
    onClick: () -> Unit
) {
    OutlinedCard(
        modifier = modifier,
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        shape = MaterialTheme.shapes.small,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, style = MaterialTheme.typography.titleSmall)
            Text(title, style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Light,
                color = MaterialTheme.colorScheme.outline
            ))


        }
    }
}

@Preview
@Composable
private fun FeatureCardPreview() {
    SMCSMonitoringSystemTheme {
        FeatureCard(
            title = "Title",
            value = "Description"
        ) { }
    }

}