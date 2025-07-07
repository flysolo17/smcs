package com.jmballangca.smcsmonitoringsystem.presentation.ui.admin

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jmballangca.smcsmonitoringsystem.presentation.navigation.AdminNavGraph
import com.jmballangca.smcsmonitoringsystem.presentation.navigation.BottomNavItem
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(modifier: Modifier = Modifier,mainNavController: NavHostController
) {
    val navController = rememberNavController()
    val items = BottomNavItem.ADMIN
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination
    val currentDestination = items.find { it.route == currentRoute }

    val primaryColor = MaterialTheme.colorScheme.primary

//    val activity = LocalActivity.current as Activity
//    val statusBarColor = MaterialTheme.colorScheme.primary
//
//
//    activity.window.statusBarColor = statusBarColor.toArgb()
//
//    // Set status bar icons color (dark or light)
//    val insetsController = WindowCompat.getInsetsController(activity.window, activity.window.decorView)
//    insetsController.isAppearanceLightStatusBars = false


    Scaffold(
        modifier = Modifier.fillMaxSize(),
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

        AdminNavGraph(
            modifier = modifier
                .fillMaxSize()
                .padding(it),
            navController = navController,
            mainNavController = mainNavController
        )
    }
}

//@Composable
//fun BottomNavigationBar(
//    modifier: Modifier = Modifier,
//    items : List<BottomNavItem<*>>,
//
//) {
//
//
//}