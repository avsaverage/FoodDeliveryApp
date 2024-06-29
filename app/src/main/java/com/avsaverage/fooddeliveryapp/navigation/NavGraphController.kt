package com.avsaverage.fooddeliveryapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.avsaverage.fooddeliveryapp.presentation.signin_screen.SignInScreen
import com.avsaverage.fooddeliveryapp.presentation.signup_screen.SignUpScreen


@Composable
fun NavGraphController(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "SignInGraph"
    ) {

        navigation(
            route = "SignInGraph",
            startDestination = Screens.SignUpScreen.route
        ){
            composable(route = Screens.SignInScreen.route) {
                SignInScreen(navController = navController)
            }
            composable(route = Screens.SignUpScreen.route) {
                SignUpScreen(navController = navController)
            }
        }

    }

}