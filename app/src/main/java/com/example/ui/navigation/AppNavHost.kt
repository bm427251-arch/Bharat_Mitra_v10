package com.example.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.screens.*
import com.example.viewmodel.MainViewModel

object AppRoutes {
    const val ROUTE_SPLASH = "splash"
    const val ROUTE_HOME = "home"
    const val ROUTE_RENT_A_CAR = "rent_a_car"
    const val ROUTE_HIRE_DRIVER = "hire_driver"
    const val ROUTE_ELITE = "elite"
    const val ROUTE_ELITE_SERVICE = "elite" // alias

    const val ROUTE_RIDER_CATEGORY = "rider_category"
    const val ROUTE_DRIVER_CATEGORY = "driver_category"
    const val ROUTE_RENT_CATEGORY = "rent_category"
    const val ROUTE_PROFILE_CREATE = "profile_create/{role}/{vehicleType}"
    const val ROUTE_DRIVER_REGISTRATION = "driver_registration/{vehicleType}"
    const val ROUTE_RENT_REGISTRATION = "rent_registration"
    const val ROUTE_MAP_VIEW = "map_view"
    const val ROUTE_ADMIN_PANEL = "admin_panel"

    fun buildProfileCreateRoute(role: String, vehicleType: String): String =
        "profile_create/$role/$vehicleType"

    fun buildDriverRegistrationRoute(vehicleType: String): String =
        "driver_registration/$vehicleType"
}

@Composable
fun AppNavHost(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = AppRoutes.ROUTE_SPLASH
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(AppRoutes.ROUTE_SPLASH) {
            SplashScreen(
                onSplashComplete = {
                    navController.navigate(AppRoutes.ROUTE_HOME) {
                        popUpTo(AppRoutes.ROUTE_SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(AppRoutes.ROUTE_HOME) {
            HomeScreen(
                viewModel = viewModel,
                onNavigateToRentCar = {
                    navController.navigate(AppRoutes.ROUTE_RENT_A_CAR) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onNavigateToHireDriver = {
                    navController.navigate(AppRoutes.ROUTE_HIRE_DRIVER) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onNavigateToElite = {
                    navController.navigate(AppRoutes.ROUTE_ELITE) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onNavigateToRiderCategory = { navController.navigate(AppRoutes.ROUTE_RIDER_CATEGORY) },
                onNavigateToDriverCategory = { navController.navigate(AppRoutes.ROUTE_DRIVER_CATEGORY) },
                onNavigateToRentCategory = { navController.navigate(AppRoutes.ROUTE_RENT_CATEGORY) }
            )
        }

        composable(AppRoutes.ROUTE_RENT_A_CAR) {
            RentACarScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onNavigateToRentRegistration = { navController.navigate(AppRoutes.ROUTE_RENT_REGISTRATION) }
            )
        }

        composable(AppRoutes.ROUTE_HIRE_DRIVER) {
            HireDriverScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onNavigateToDriverCategory = { navController.navigate(AppRoutes.ROUTE_DRIVER_CATEGORY) }
            )
        }

        composable(AppRoutes.ROUTE_ELITE) {
            EliteScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(AppRoutes.ROUTE_RIDER_CATEGORY) {
            RiderCategoryScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onCreateProfile = { vehicleType ->
                    navController.navigate(AppRoutes.buildProfileCreateRoute("RIDER", vehicleType))
                }
            )
        }

        composable(AppRoutes.ROUTE_DRIVER_CATEGORY) {
            DriverCategoryScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onCreateProfile = { vehicleType ->
                    navController.navigate(AppRoutes.buildDriverRegistrationRoute(vehicleType))
                }
            )
        }

        composable(AppRoutes.ROUTE_RENT_CATEGORY) {
            RentACarCategoryScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onCreateProfile = {
                    navController.navigate(AppRoutes.ROUTE_RENT_REGISTRATION)
                }
            )
        }

        composable(
            route = AppRoutes.ROUTE_PROFILE_CREATE,
            arguments = listOf(
                navArgument("role") { type = NavType.StringType },
                navArgument("vehicleType") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val role = backStackEntry.arguments?.getString("role") ?: "RIDER"
            val vehicleType = backStackEntry.arguments?.getString("vehicleType") ?: "BIKE"
            ProfileCreateScreen(
                viewModel = viewModel,
                role = role,
                vehicleType = vehicleType,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = AppRoutes.ROUTE_DRIVER_REGISTRATION,
            arguments = listOf(
                navArgument("vehicleType") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val vehicleType = backStackEntry.arguments?.getString("vehicleType") ?: "Sedan"
            DriverRegistrationScreen(
                viewModel = viewModel,
                vehicleType = vehicleType,
                onBack = { navController.popBackStack() }
            )
        }

        composable(AppRoutes.ROUTE_RENT_REGISTRATION) {
            RentACarRegistrationScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(AppRoutes.ROUTE_MAP_VIEW) {
            MapScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(AppRoutes.ROUTE_ADMIN_PANEL) {
            AdminPanelScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
