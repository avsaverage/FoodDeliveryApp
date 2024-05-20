package com.avsaverage.fooddeliveryapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.avsaverage.fooddeliveryapp.presentation.screens.CartScreen
import com.avsaverage.fooddeliveryapp.presentation.login_screen.LoginScreen
import com.avsaverage.fooddeliveryapp.presentation.screens.MainScreen
import com.avsaverage.fooddeliveryapp.presentation.signup_screen.RegisterScreen

sealed class NavRoute(val route: String) {
    data object LoginScren : NavRoute("")
    data object RegisterScreen : NavRoute("register_screen")
    data object MainScreen : NavRoute("main_screen")
    data object CartScreen : NavRoute("cart_screen")
}
@Preview(showBackground = true)
@Composable
fun ScreensNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "LoginScreen") {
        composable("LoginScreen") { LoginScreen(navController = navController) }
        composable("MainScreen") { MainScreen(navController = navController) }
        composable("CartScreen") { CartScreen(navController = navController) }
        composable("RegisterScreen") { RegisterScreen(navController = navController) }
    }
}