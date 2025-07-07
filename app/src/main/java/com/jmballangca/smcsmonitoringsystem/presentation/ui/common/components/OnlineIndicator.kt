package com.jmballangca.smcsmonitoringsystem.presentation.ui.common.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp



@Composable
fun OnlineIndicator(
    modifier: Modifier = Modifier,
    online: Boolean,

) {
    val color : Color = if (online) Color.Green else Color.Gray
    Box(
        modifier = modifier.size(8.dp).background(
            color = color,
            shape = CircleShape
        )
    ){

    }

}