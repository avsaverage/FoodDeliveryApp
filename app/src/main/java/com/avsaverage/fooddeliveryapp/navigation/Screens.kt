package com.avsaverage.fooddeliveryapp.navigation

sealed class Screens(val route: String) {
    data object SignInScreen : Screens(route = "SignIn_Screen")
    data object SignUpScreen : Screens(route = "SignUp_Screen")
}