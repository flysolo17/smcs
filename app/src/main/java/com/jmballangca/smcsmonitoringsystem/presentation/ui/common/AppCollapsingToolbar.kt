package com.jmballangca.smcsmonitoringsystem.presentation.ui.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.fontscaling.MathUtils.lerp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppCollapsingToolbar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    name : String,
    description : String,
    values : String,
    title : @Composable () -> Unit,
    navigationIcon: @Composable () -> Unit,
    actions: @Composable () -> Unit
) {
    val maxImageHeight = 250.dp
    val minImageHeight = 0.dp
    val collapseFraction = scrollBehavior.state.collapsedFraction
    val imageHeightPx = with(LocalDensity.current) {
        lerp(
            maxImageHeight.toPx(),
            minImageHeight.toPx(),
            collapseFraction.coerceIn(0f, 1f)
        )
    }
    val imageHeight = with(LocalDensity.current) { imageHeightPx.toDp() }


    Box(modifier = modifier) {
        // 📷 Collapsing image/banner area

        // 🧭 Top bar overlaid on top of image
        LargeTopAppBar(
            expandedHeight = maxImageHeight,
            title = {
                if (collapseFraction > 0.5f) title()
            },
            navigationIcon = navigationIcon,
            actions = { actions() },
            scrollBehavior = scrollBehavior,
            colors = TopAppBarDefaults.largeTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                scrolledContainerColor = MaterialTheme.colorScheme.primary, // <-- 👈 important
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                actionIconContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight)
                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                name,
                modifier = Modifier.padding(12.dp),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
            Text(description,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.outline
                ),
                textAlign = TextAlign.Center
            )
            Text(values, style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.outline),  textAlign = TextAlign.Center)
            Spacer(
                modifier = Modifier
                    .height(16.dp)
            )
            actions()
        }

    }
}