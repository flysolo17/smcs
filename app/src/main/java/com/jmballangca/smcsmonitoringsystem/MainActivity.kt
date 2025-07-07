package com.jmballangca.smcsmonitoringsystem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jmballangca.smcsmonitoringsystem.presentation.navigation.ADMIN
import com.jmballangca.smcsmonitoringsystem.presentation.navigation.AUTH
import com.jmballangca.smcsmonitoringsystem.presentation.navigation.CHECKER
import com.jmballangca.smcsmonitoringsystem.presentation.navigation.HAULER
import com.jmballangca.smcsmonitoringsystem.presentation.navigation.MRF_CHECKERS
import com.jmballangca.smcsmonitoringsystem.presentation.navigation.TENANT

import com.jmballangca.smcsmonitoringsystem.presentation.navigation.authNavGraph
import com.jmballangca.smcsmonitoringsystem.presentation.ui.admin.AdminScreen
import com.jmballangca.smcsmonitoringsystem.presentation.ui.admin.checkers.CheckerScreen
import com.jmballangca.smcsmonitoringsystem.presentation.ui.admin.hauler.HaulersScreen
import com.jmballangca.smcsmonitoringsystem.presentation.ui.admin.tenant.TenantScreen
import com.jmballangca.smcsmonitoringsystem.presentation.ui.checker.CheckerMain


import com.jmballangca.smcsmonitoringsystem.ui.theme.SMCSMonitoringSystemTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SMCSMonitoringSystemTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = AUTH
                ) {
                    authNavGraph(navController)
                    composable<ADMIN> {
                        AdminScreen(
                            mainNavController = navController
                        )
                    }
                    composable<CHECKER> {
                        CheckerMain(
                            mainNavController = navController
                        )
                    }
                    composable<TENANT> {
                        TenantScreen(
                            navController = navController
                        )
                    }
                    composable<HAULER> {
                        HaulersScreen(
                            navController = navController
                        )
                    }
                    composable<MRF_CHECKERS> {
                        CheckerScreen(
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}

