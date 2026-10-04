package com.example

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.ScreenState
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val viewModel: MainViewModel = viewModel()
                val currentScreen by viewModel.currentScreen.collectAsState()

                // Permission launcher for Location (Auto-detect pickup GPS)
                val locationPermissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions()
                ) { permissions ->
                    val isGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
                    if (isGranted) {
                        viewModel.autoDetectPickupLocation()
                    }
                }

                LaunchedEffect(Unit) {
                    locationPermissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }

                // Requirement 6: Back button handling
                // Pressing back brings user to Home screen; double press on Home exits
                BackHandler {
                    viewModel.handleBackPress(onExitApp = { finish() })
                }

                Surface(modifier = Modifier.fillMaxSize()) {
                    AnimatedContent(
                        targetState = currentScreen,
                        label = "screen_navigation_anim"
                    ) { targetScreen ->
                        when (targetScreen) {
                            ScreenState.SPLASH -> {
                                SplashScreen(
                                    viewModel = viewModel,
                                    onNavigateToHome = { viewModel.navigateTo(ScreenState.HOME) }
                                )
                            }
                            ScreenState.HOME -> {
                                HomeScreen(
                                    viewModel = viewModel,
                                    onNavigateToRentCar = { viewModel.navigateTo(ScreenState.RENT_A_CAR) },
                                    onNavigateToHireDriver = { viewModel.navigateTo(ScreenState.HIRE_DRIVER) },
                                    onNavigateToElite = { viewModel.navigateTo(ScreenState.ELITE_SERVICE) },
                                    onNavigateToRiderCategory = { viewModel.navigateTo(ScreenState.RIDER_CATEGORY) },
                                    onNavigateToDriverCategory = { viewModel.navigateTo(ScreenState.DRIVER_CATEGORY) },
                                    onNavigateToRentCategory = { viewModel.navigateTo(ScreenState.RENT_CATEGORY) }
                                )
                            }
                            ScreenState.RIDER_CATEGORY -> {
                                RiderCategoryScreen(
                                    viewModel = viewModel,
                                    onBack = { viewModel.navigateTo(ScreenState.HOME) },
                                    onCreateProfile = { vehicleType ->
                                        viewModel.setSelectedCategory("RIDER", vehicleType)
                                        viewModel.navigateTo(ScreenState.PROFILE_CREATE)
                                    }
                                )
                            }
                            ScreenState.DRIVER_CATEGORY -> {
                                DriverCategoryScreen(
                                    viewModel = viewModel,
                                    onBack = { viewModel.navigateTo(ScreenState.HOME) },
                                    onCreateProfile = { vehicleType ->
                                        viewModel.setSelectedCategory("DRIVER", vehicleType)
                                        viewModel.navigateTo(ScreenState.DRIVER_REGISTRATION)
                                    }
                                )
                            }
                            ScreenState.RENT_CATEGORY -> {
                                RentACarCategoryScreen(
                                    viewModel = viewModel,
                                    onBack = { viewModel.navigateTo(ScreenState.HOME) },
                                    onCreateProfile = {
                                        viewModel.setSelectedCategory("RENT_A_CAR", "")
                                        viewModel.navigateTo(ScreenState.RENT_REGISTRATION)
                                    }
                                )
                            }
                            ScreenState.PROFILE_CREATE -> {
                                val selectedRole by viewModel.selectedRole.collectAsState()
                                val selectedVehicle by viewModel.selectedVehicleType.collectAsState()
                                ProfileCreateScreen(
                                    viewModel = viewModel,
                                    role = selectedRole,
                                    vehicleType = selectedVehicle,
                                    onBack = { viewModel.navigateTo(ScreenState.RIDER_CATEGORY) }
                                )
                            }
                            ScreenState.DRIVER_REGISTRATION -> {
                                val selectedVehicle by viewModel.selectedVehicleType.collectAsState()
                                DriverRegistrationScreen(
                                    viewModel = viewModel,
                                    vehicleType = selectedVehicle,
                                    onBack = { viewModel.navigateTo(ScreenState.DRIVER_CATEGORY) }
                                )
                            }
                            ScreenState.RENT_REGISTRATION -> {
                                RentACarRegistrationScreen(
                                    viewModel = viewModel,
                                    onBack = { viewModel.navigateTo(ScreenState.RENT_CATEGORY) }
                                )
                            }
                            ScreenState.RENT_A_CAR -> {
                                RentACarScreen(
                                    viewModel = viewModel,
                                    onBack = { viewModel.navigateTo(ScreenState.HOME) }
                                )
                            }
                            ScreenState.HIRE_DRIVER -> {
                                HireDriverScreen(
                                    viewModel = viewModel,
                                    onBack = { viewModel.navigateTo(ScreenState.HOME) }
                                )
                            }
                            ScreenState.ELITE_SERVICE -> {
                                EliteScreen(
                                    viewModel = viewModel,
                                    onBack = { viewModel.navigateTo(ScreenState.HOME) }
                                )
                            }
                            ScreenState.ADMIN_PANEL -> {
                                AdminPanelScreen(
                                    viewModel = viewModel,
                                    onBack = { viewModel.navigateTo(ScreenState.HOME) }
                                )
                            }
                            ScreenState.ACTIVE_RIDE_TRACKING -> {
                                ActiveRideTrackingScreen(
                                    viewModel = viewModel,
                                    onBackToHome = { viewModel.navigateTo(ScreenState.HOME) }
                                )
                            }
                            ScreenState.MAP_VIEW -> {
                                MapScreen(
                                    viewModel = viewModel,
                                    onBack = { viewModel.navigateTo(ScreenState.HOME) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
