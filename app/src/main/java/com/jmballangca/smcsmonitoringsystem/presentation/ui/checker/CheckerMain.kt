package com.jmballangca.smcsmonitoringsystem.presentation.ui.checker

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jmballangca.smcsmonitoringsystem.presentation.navigation.AdminNavGraph
import com.jmballangca.smcsmonitoringsystem.presentation.navigation.BottomNavItem
import com.jmballangca.smcsmonitoringsystem.presentation.navigation.CheckerNavGraph


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckerMain(
    modifier: Modifier = Modifier,
    mainNavController: NavHostController
) {
    val navController = rememberNavController()
    val items = BottomNavItem.CHECKER
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination
    val currentDestination = items.find { it.route == currentRoute }

    Scaffold(
        bottomBar = {
            BottomAppBar {
                items.forEach { item ->
                    val selected = currentRoute?.hierarchy?.any { it.hasRoute(
                        item.route::class
                    ) } == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(item.route)
                        },
                        label = {
                            Text(text = item.name)
                        },
                        icon = {
                            BadgedBox(
                                badge = {
                                    when {
                                        item.badgeCount != null -> Badge {
                                            Text("${item.badgeCount}")
                                        }
                                        item.hasNews -> Badge()
                                    }
                                }
                            ) {

                                Icon(
                                    imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.name
                                )
                            }

                        }
                    )
                }
            }
        }
    ) {
        CheckerNavGraph(
            modifier = modifier
                .fillMaxSize()
                .padding(it),
            navController = navController,
            mainNavController = mainNavController
        )
    }
}
