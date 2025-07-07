package com.jmballangca.smcsmonitoringsystem.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jmballangca.smcsmonitoringsystem.presentation.ui.admin.home.HomeScreen
import com.jmballangca.smcsmonitoringsystem.presentation.ui.admin.logs.LogScreen
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.profile.ProfileScreen


@Composable
fun AdminNavGraph(
    modifier: Modifier = Modifier,
    mainNavController: NavHostController,
    navController : NavHostController,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = HOME
    ) {
        composable<HOME> {
            HomeScreen(
                navController = navController,
                mainNavController = mainNavController
            )
        }
        composable<LOGS> {
            LogScreen()
        }
        composable<PROFILE> {
            ProfileScreen(
                mainNavController = mainNavController,
                navController = navController
            )
        }

    }
}