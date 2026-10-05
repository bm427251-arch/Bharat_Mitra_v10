package com.example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.navigation.AppNavHost
import com.example.ui.navigation.AppRoutes
import com.example.ui.theme.BharatDarkBlue
import com.example.viewmodel.MainViewModel

data class BottomNavItem(
    val title: String,
    val route: String,
    val icon: ImageVector,
    val testTag: String
)

@Composable
fun MainScreen(
    viewModel: MainViewModel
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Bottom Nav: Home, Bookings, Wallet, Profile
    val navItems = listOf(
        BottomNavItem("Home", AppRoutes.ROUTE_HOME, Icons.Default.Home, "bottom_nav_home"),
        BottomNavItem("Bookings", AppRoutes.ROUTE_AVAILABLE_FLEET, Icons.Default.DirectionsCar, "bottom_nav_bookings"),
        BottomNavItem("Wallet", AppRoutes.ROUTE_PAYMENT_HISTORY, Icons.Default.AccountBalanceWallet, "bottom_nav_wallet"),
        BottomNavItem("Profile", AppRoutes.ROUTE_CREATE_DRIVER, Icons.Default.Person, "bottom_nav_profile")
    )

    val isSplashScreen = currentRoute == AppRoutes.ROUTE_SPLASH || currentRoute == null

    Scaffold(
        bottomBar = {
            if (!isSplashScreen) {
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 8.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("fixed_main_bottom_nav_bar")
                ) {
                    navItems.forEach { item ->
                        val isSelected = currentRoute == item.route
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                if (currentRoute != item.route) {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title,
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = item.title,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = BharatDarkBlue,
                                selectedTextColor = BharatDarkBlue,
                                indicatorColor = BharatDarkBlue.copy(alpha = 0.12f),
                                unselectedIconColor = Color.Gray,
                                unselectedTextColor = Color.Gray
                            ),
                            modifier = Modifier.testTag(item.testTag)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        AppNavHost(
            viewModel = viewModel,
            navController = navController,
            modifier = if (isSplashScreen) Modifier else Modifier.padding(innerPadding)
        )
    }
}
