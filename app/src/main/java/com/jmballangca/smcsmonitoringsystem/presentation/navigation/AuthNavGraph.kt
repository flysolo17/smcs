package com.jmballangca.smcsmonitoringsystem.presentation.navigation

import androidx.compose.material3.Text
import androidx.navigation.NavGraph
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.jmballangca.smcsmonitoringsystem.presentation.ui.auth.login.LoginScreen


fun NavGraphBuilder.authNavGraph(
    navController: NavHostController
) {
    navigation<AUTH>(
        startDestination = LOGIN
    ) {
        composable<LOGIN> {
            LoginScreen(
                navController = navController
            )
        }
    }

}