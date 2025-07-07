package com.jmballangca.smcsmonitoringsystem.presentation.ui.common.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FireTruck
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jmballangca.smcsmonitoringsystem.data.model.Hauler


@Composable
fun HaulerListItem(
    modifier: Modifier = Modifier,
    hauler: Hauler,
    onClick : () -> Unit = {},
    onDelete: () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth().padding(
            vertical = 4.dp
        ),
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        ) {
        Row(
            modifier = Modifier.padding(
                12.dp
            ),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy( 8.dp )
        ) {
            Icon(
                imageVector = Icons.Filled.FireTruck,
                contentDescription = hauler.name
            )
            Text(text = hauler.name,style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            FilledTonalIconButton(
                onClick = onDelete,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.onError
                )
            }

        }


    }
}