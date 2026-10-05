package com.example.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.screens.*
import com.example.viewmodel.MainViewModel

object AppRoutes {
    const val ROUTE_SPLASH = "splash"
    const val ROUTE_HOME = "home"
    const val ROUTE_AVAILABLE_FLEET = "available_fleet"
    const val ROUTE_BOOKING_CONFIRMED = "booking_confirmed"
    const val ROUTE_AUTO_QR_PAYMENT = "auto_qr_payment"
    const val ROUTE_LIVE_TRACKING = "live_tracking"
    const val ROUTE_PAYMENT_HISTORY = "payment_history"
    const val ROUTE_CREATE_DRIVER = "create_driver"
    const val ROUTE_RENT_A_CAR_WALL = "rent_a_car_wall"
    const val ROUTE_RENT_OWNER_FORM = "rent_owner_form"
    const val ROUTE_HIRE_DRIVER_WALL = "hire_driver_wall"
    const val ROUTE_HIRE_DRIVER_FORM = "hire_driver_form"
    const val ROUTE_RENT_SUBSCRIPTION = "rent_subscription"
    const val ROUTE_HIRE_DRIVER_SUBSCRIPTION = "hire_driver_subscription"
    const val ROUTE_ADMIN_DASHBOARD = "admin_dashboard"
    const val ROUTE_RATING = "rating"
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
        // Page 1: Splash Screen
        composable(AppRoutes.ROUTE_SPLASH) {
            SplashScreen(
                onSplashComplete = {
                    navController.navigate(AppRoutes.ROUTE_HOME) {
                        popUpTo(AppRoutes.ROUTE_SPLASH) { inclusive = true }
                    }
                }
            )
        }

        // Page 2: Home Booking
        composable(AppRoutes.ROUTE_HOME) {
            HomeScreen(
                viewModel = viewModel,
                onNavigateToFleetMap = { navController.navigate(AppRoutes.ROUTE_AVAILABLE_FLEET) },
                onNavigateToRentCar = { navController.navigate(AppRoutes.ROUTE_RENT_A_CAR_WALL) },
                onNavigateToHireDriver = { navController.navigate(AppRoutes.ROUTE_HIRE_DRIVER_WALL) },
                onNavigateToCreateDriver = { navController.navigate(AppRoutes.ROUTE_CREATE_DRIVER) },
                onNavigateToWallet = { navController.navigate(AppRoutes.ROUTE_PAYMENT_HISTORY) },
                onNavigateToAdmin = { navController.navigate(AppRoutes.ROUTE_ADMIN_DASHBOARD) },
                onNavigateToAutoQr = { navController.navigate(AppRoutes.ROUTE_AUTO_QR_PAYMENT) }
            )
        }

        // Page 3: Available Fleet Map
        composable(AppRoutes.ROUTE_AVAILABLE_FLEET) {
            AvailableFleetMapScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onConfirmBooking = { navController.navigate(AppRoutes.ROUTE_BOOKING_CONFIRMED) }
            )
        }

        // Page 4: Booking Confirmed
        composable(AppRoutes.ROUTE_BOOKING_CONFIRMED) {
            BookingConfirmedScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onTrackRide = { navController.navigate(AppRoutes.ROUTE_LIVE_TRACKING) },
                onOpenAutoQrPayment = { navController.navigate(AppRoutes.ROUTE_AUTO_QR_PAYMENT) }
            )
        }

        // Page 5: Auto QR Payment
        composable(AppRoutes.ROUTE_AUTO_QR_PAYMENT) {
            AutoQrPaymentScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onPaymentSuccess = { navController.navigate(AppRoutes.ROUTE_PAYMENT_HISTORY) }
            )
        }

        // Page 6: Live Tracking
        composable(AppRoutes.ROUTE_LIVE_TRACKING) {
            LiveTrackingScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onFinishRideAndRate = { navController.navigate(AppRoutes.ROUTE_RATING) }
            )
        }

        // Page 7: Payment History & Wallet
        composable(AppRoutes.ROUTE_PAYMENT_HISTORY) {
            PaymentHistoryScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        // Page 8: Create Driver Profile
        composable(AppRoutes.ROUTE_CREATE_DRIVER) {
            CreateDriverProfileScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        // Page 9: Rent A Car Wall
        composable(AppRoutes.ROUTE_RENT_A_CAR_WALL) {
            RentACarWallScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onBecomeRentOwner = { navController.navigate(AppRoutes.ROUTE_RENT_OWNER_FORM) },
                onBookCar = { navController.navigate(AppRoutes.ROUTE_AUTO_QR_PAYMENT) }
            )
        }

        // Page 10: Become Rent Owner Any Vehicle
        composable(AppRoutes.ROUTE_RENT_OWNER_FORM) {
            RentOwnerFormScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onContinueToSubscription = { navController.navigate(AppRoutes.ROUTE_RENT_SUBSCRIPTION) }
            )
        }

        // Page 11: Hire Driver Wall
        composable(AppRoutes.ROUTE_HIRE_DRIVER_WALL) {
            HireDriverWallScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onBecomeHireDriver = { navController.navigate(AppRoutes.ROUTE_HIRE_DRIVER_FORM) }
            )
        }

        // Page 12: Become Hire Driver (DL Only, No RC)
        composable(AppRoutes.ROUTE_HIRE_DRIVER_FORM) {
            HireDriverFormScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        // Page 13: Rent Subscription ₹299/mo
        composable(AppRoutes.ROUTE_RENT_SUBSCRIPTION) {
            RentSubscriptionScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        // Page 14: Hire Driver Subscription ₹299/mo
        composable(AppRoutes.ROUTE_HIRE_DRIVER_SUBSCRIPTION) {
            HireDriverSubscriptionScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        // Page 15: Admin Dashboard
        composable(AppRoutes.ROUTE_ADMIN_DASHBOARD) {
            AdminDashboardScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        // Page 16: Rating Page
        composable(AppRoutes.ROUTE_RATING) {
            RatingScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onFinished = {
                    navController.navigate(AppRoutes.ROUTE_HOME) {
                        popUpTo(AppRoutes.ROUTE_HOME) { inclusive = false }
                    }
                }
            )
        }
    }
}
