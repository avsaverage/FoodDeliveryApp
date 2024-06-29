package com.avsaverage.fooddeliveryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.avsaverage.fooddeliveryapp.navigation.BottomNavigationItemManager
import com.avsaverage.fooddeliveryapp.navigation.BottomNavigationItemUser
import com.avsaverage.fooddeliveryapp.navigation.Screens
import com.avsaverage.fooddeliveryapp.presentation.manager.menu_screen.MenuScreenManager
import com.avsaverage.fooddeliveryapp.presentation.manager.order_screen.OrderScreenManager
import com.avsaverage.fooddeliveryapp.presentation.user.cart_screen.CartScreen
import com.avsaverage.fooddeliveryapp.presentation.user.menu_screen.MenuScreen
import com.avsaverage.fooddeliveryapp.presentation.user.order_screen.OrderScreen
import com.avsaverage.fooddeliveryapp.ui.theme.FoodDeliveryAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManagerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FoodDeliveryAppTheme() {
                val navController = rememberNavController()

                var selectedItemIndex by rememberSaveable {
                    mutableStateOf(0)
                }

                val items = listOf(
                    BottomNavigationItemManager.Menu,
                    BottomNavigationItemManager.Orders
                )

                Surface {
                    Scaffold(
                        bottomBar = {
                            NavigationBar {
                                items.forEachIndexed { index, item ->
                                    NavigationBarItem(
                                        selected = selectedItemIndex == index,
                                        onClick = {
                                            selectedItemIndex = index
                                            navController.navigate(item.title) {
                                                popUpTo(navController.graph.findStartDestination().id)
                                                {
                                                    inclusive = true
                                                }
                                                launchSingleTop = true
                                            }
                                        },
                                        label = {
                                            Text(text = item.title)
                                        },
                                        icon = {
                                            BadgedBox(
                                                badge = {
                                                    if(item.badgeCount != null) {
                                                        Badge{
                                                            Text(text = item.badgeCount.toString())
                                                        }
                                                    } else if(item.hasNews)
                                                    {
                                                        Badge()
                                                    }
                                                }
                                            ) {
                                                Icon(
                                                    imageVector = if(index == selectedItemIndex) {
                                                        item.selectedIcon
                                                    } else item.unselectedIcon,
                                                    contentDescription = item.title
                                                )

                                            }
                                        }
                                    )
                                }
                            }
                        }
                    ) {
                        Surface(
                            modifier = Modifier.padding(it)
                        ) {
                            NavHost(
                                navController = navController,
                                startDestination = Screens.MenuManagerScreen.route
                            )
                            {
                                composable(route = Screens.MenuManagerScreen.route)
                                {
                                    MenuScreenManager(navController = navController)
                                }
                                composable(route = Screens.OrdersManagerScreen.route)
                                {
                                    OrderScreenManager()
                                }

                            }
                        }
                    }
                }
            }
        }
    }
}