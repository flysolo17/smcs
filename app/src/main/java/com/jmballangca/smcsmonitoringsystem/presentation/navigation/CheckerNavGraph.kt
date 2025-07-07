package com.jmballangca.smcsmonitoringsystem.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jmballangca.smcsmonitoringsystem.presentation.ui.admin.home.HomeScreen
import com.jmballangca.smcsmonitoringsystem.presentation.ui.admin.logs.LogScreen
import com.jmballangca.smcsmonitoringsystem.presentation.ui.checker.home.CheckerHome
import com.jmballangca.smcsmonitoringsystem.presentation.ui.checker.logs.CheckerLogScreen
import com.jmballangca.smcsmonitoringsystem.presentation.ui.common.profile.ProfileScreen


@Composable
fun CheckerNavGraph(
    modifier: Modifier = Modifier,
    mainNavController: NavHostController,
    navController : NavHostController,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = CHECKER_HOME
    ) {
        composable<CHECKER_HOME> {
            CheckerHome(
                navHostController = navController
            )
        }
        composable<LOGS> {
            CheckerLogScreen()
        }
        composable<PROFILE> {
            ProfileScreen(
                mainNavController = mainNavController,
                navController = navController
            )
        }

    }
}

