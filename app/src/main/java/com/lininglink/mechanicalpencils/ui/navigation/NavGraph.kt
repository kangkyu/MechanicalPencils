package com.lininglink.mechanicalpencils.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lininglink.mechanicalpencils.ui.screens.MainScreen
import com.lininglink.mechanicalpencils.ui.screens.auth.AuthViewModel
import com.lininglink.mechanicalpencils.ui.screens.auth.LoginScreen
import com.lininglink.mechanicalpencils.ui.screens.auth.RegisterScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppNavGraph(
    authViewModel: AuthViewModel = koinViewModel()
) {
    val navController = rememberNavController()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
    val uiState by authViewModel.uiState.collectAsState()

    // Get user email from successful auth state
    val userEmail = when (val state = uiState) {
        is com.lininglink.mechanicalpencils.ui.screens.auth.AuthUiState.Success -> state.user.email
        else -> null
    }

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            navController.navigate(MainTabs) {
                popUpTo(Login) { inclusive = true }
            }
        } else {
            navController.navigate(Login) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) MainTabs else Login
    ) {
        composable<Login> {
            LoginScreen(
                viewModel = authViewModel,
                onNavigateToRegister = {
                    navController.navigate(Register)
                },
                onLoginSuccess = {
                    navController.navigate(MainTabs) {
                        popUpTo(Login) { inclusive = true }
                    }
                }
            )
        }

        composable<Register> {
            RegisterScreen(
                viewModel = authViewModel,
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onRegisterSuccess = {
                    navController.navigate(MainTabs) {
                        popUpTo(Login) { inclusive = true }
                    }
                }
            )
        }

        composable<MainTabs> {
            MainScreen(
                userEmail = userEmail,
                onLogout = {
                    authViewModel.logout()
                }
            )
        }
    }
}
