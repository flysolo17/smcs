package com.jmballangca.smcsmonitoringsystem.presentation.ui.common.components


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jmballangca.smcsmonitoringsystem.data.model.User


@Composable
fun CheckersListItem(
    modifier: Modifier = Modifier,
    user: User,
    onClick : () -> Unit = {},
    onDelete : () -> Unit = {}
) {
    Card(
        modifier = modifier.fillMaxWidth().padding(
            vertical = 4.dp
        ),
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
        ){
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Person",
                tint = Color.Gray
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    Text(
                        text = user.name,
                        style = MaterialTheme.typography.titleMedium
                    )
                    val online = user.online
                    OnlineIndicator(
                        online = online
                    )
                }
                Text(
                    text = user.username,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
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