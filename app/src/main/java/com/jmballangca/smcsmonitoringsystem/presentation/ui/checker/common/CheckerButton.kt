package com.jmballangca.smcsmonitoringsystem.presentation.ui.checker.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.jmballangca.smcsmonitoringsystem.ui.theme.onPrimaryContainerLight


@Composable
fun CheckerButton(
    modifier: Modifier = Modifier,
    icon : ImageVector,
    label : String,
    color : Color = onPrimaryContainerLight,
    labelColor : Color = Color.White,
    onClick : () -> Unit
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = color,
            contentColor = labelColor
        ),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium
            )
        }

    }

}