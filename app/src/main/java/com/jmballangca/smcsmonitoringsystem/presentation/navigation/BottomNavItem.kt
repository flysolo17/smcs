package com.jmballangca.smcsmonitoringsystem.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Recycling
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem<T: Any>(
    val name: String,
    val route: T,
    val badgeCount: Int? = null,
    val hasNews: Boolean = false,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
) {
    companion object {
        val ADMIN = listOf(
            BottomNavItem(
                name = "Home",
                route = HOME,
                selectedIcon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home,
            ),
            BottomNavItem(
                name = "Logs",
                route = LOGS,
                selectedIcon = Icons.Filled.Recycling,
                unselectedIcon = Icons.Outlined.Recycling,
            ),
            BottomNavItem(
                name = "Profile",
                route = PROFILE,
                selectedIcon = Icons.Filled.Person,
                unselectedIcon = Icons.Outlined.Person,
            ),
        )
        val CHECKER = listOf(
            BottomNavItem(
                name = "Home",
                route = CHECKER_HOME,
                selectedIcon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home,
            ),
            BottomNavItem(
                name = "Logs",
                route = LOGS,
                selectedIcon = Icons.Filled.Recycling,
                unselectedIcon = Icons.Outlined.Recycling,
            ),
            BottomNavItem(
                name = "Profile",
                route = PROFILE,
                selectedIcon = Icons.Filled.Person,
                unselectedIcon = Icons.Outlined.Person,
            )
        )
    }
}
