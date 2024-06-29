package com.avsaverage.fooddeliveryapp.navigation

import android.accessibilityservice.AccessibilityService.ScreenshotResult
import android.widget.ImageView.ScaleType

sealed class Screens(val route: String) {
    data object SignInScreen : Screens(route = "Вход")
    data object SignUpScreen : Screens(route = "Регистрация")
    data object MenuScreen : Screens(route = "Меню")
    data object CartScreen : Screens(route = "Корзина")
    data object OrderScreen: Screens(route = "Заказ")

    data object MenuManagerScreen : Screens(route = "Меню")
    data object OrdersManagerScreen : Screens(route = "Заказы")

}