package com.example.viewmodel

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    // Real-time GPS Location & Auto-detection
    val locationManager = com.example.data.location.LocationManager(application)
    val currentLocation = locationManager.currentLocation
    val currentAddress = locationManager.currentAddress
    val isLocationLoading = locationManager.isLocationLoading
    val isGpsEnabled = locationManager.isGpsEnabled

    private val _isLocationPermissionGranted = MutableStateFlow(true)
    val isLocationPermissionGranted: StateFlow<Boolean> = _isLocationPermissionGranted.asStateFlow()

    fun updateLocationPermission(granted: Boolean) {
        _isLocationPermissionGranted.value = granted
        if (granted) {
            locationManager.startLocationUpdates(viewModelScope)
        }
    }

    fun refreshLocation() {
        locationManager.requestFreshAccurateLocation(viewModelScope)
    }

    init {
        locationManager.startLocationUpdates(viewModelScope)
    }

    // Silent Push Notification for Admin (triggers 7s after form submission)
    private val _pendingVerificationCount = MutableStateFlow(2)
    val pendingVerificationCount: StateFlow<Int> = _pendingVerificationCount.asStateFlow()

    private val _silentNotification = MutableStateFlow<SilentPushNotification?>(null)
    val silentNotification: StateFlow<SilentPushNotification?> = _silentNotification.asStateFlow()

    // Admin Stats (INTERNAL ONLY - Customer side never sees 15% commission)
    val adminDriversCount = MutableStateFlow(24)
    val adminVerifiedCount = MutableStateFlow(22)
    val adminBlockedCount = MutableStateFlow(1)
    val adminBookingsToday = MutableStateFlow(18)
    val adminEarningsToday = MutableStateFlow(1500)
    // 15% Commission split: Company ₹225, Driver ₹1275
    val companyCommission = 225
    val driverPayout = 1275

    // User Wallet & Payment
    private val _walletBalance = MutableStateFlow(1200)
    val walletBalance: StateFlow<Int> = _walletBalance.asStateFlow()

    private val _paymentHistory = MutableStateFlow(
        listOf(
            PaymentTransaction("tx_1", "Ride to Central Square", 100, "UPI", "Today, 6:42 PM", true),
            PaymentTransaction("tx_2", "Auto Ride Outstation", 250, "Cash", "Yesterday, 3:15 PM", true),
            PaymentTransaction("tx_3", "Rental Vehicle Booking", 500, "Wallet", "12 Oct, 11:20 AM", true)
        )
    )
    val paymentHistory: StateFlow<List<PaymentTransaction>> = _paymentHistory.asStateFlow()

    // Fleet List (Page 3 - 6 Services)
    private val _availableFleet = MutableStateFlow(
        listOf(
            FleetItem("fl_bike", "Bike Taxi", "Fast Affordable", availableCount = 6, etaMinutes = 2, price = 40, rating = 4.8),
            FleetItem("fl_toto", "Toto E-Rickshaw", "Eco Shared", availableCount = 8, etaMinutes = 3, price = 30, rating = 4.7),
            FleetItem("fl_auto", "Auto Rickshaw", "Popular Quick", availableCount = 5, etaMinutes = 4, price = 60, rating = 4.8),
            FleetItem("fl_mini", "Mini Cab", "Budget 4 seats", availableCount = 4, etaMinutes = 5, price = 80, rating = 4.7),
            FleetItem("fl_sedan", "Sedan", "Comfort 4 seats", availableCount = 5, etaMinutes = 3, price = 100, rating = 4.9),
            FleetItem("fl_suv", "SUV", "Spacious 6 seats", availableCount = 3, etaMinutes = 6, price = 150, rating = 4.9)
        )
    )
    val availableFleet: StateFlow<List<FleetItem>> = _availableFleet.asStateFlow()

    // Rental Cars (Page 9)
    private val _rentalCars = MutableStateFlow(
        listOf(
            RentalCar("rc_1", "Maruti Swift", "Hatchback", 1500, 5, "Manual", "Petrol / CNG"),
            RentalCar("rc_2", "Hyundai i20 Asta", "Hatchback", 1800, 5, "Automatic", "Petrol"),
            RentalCar("rc_3", "Hyundai Creta SX", "SUV", 2800, 5, "Automatic", "Diesel"),
            RentalCar("rc_4", "Toyota Innova Crysta", "MUV", 3500, 7, "Manual", "Diesel")
        )
    )
    val rentalCars: StateFlow<List<RentalCar>> = _rentalCars.asStateFlow()

    // Hire Drivers (Page 11)
    private val _hireDrivers = MutableStateFlow(
        listOf(
            HireDriverItem("hd_1", "Amit Sharma", 4.9, 5, "Delhi / NCR", 150, "Top Rated • 5+ yrs Delhi", true, 2.3),
            HireDriverItem("hd_2", "Priya Kumari", 4.7, 4, "Delhi / Airport", 130, "EV Specialist • Safe Driver", true, 3.1),
            HireDriverItem("hd_3", "Rahul Mehta", 4.8, 8, "Outstation Express", 180, "Highway & Night Pro", true, 1.8)
        )
    )
    val hireDrivers: StateFlow<List<HireDriverItem>> = _hireDrivers.asStateFlow()

    // Current Booking state
    private val _currentBooking = MutableStateFlow(BookingDetails())
    val currentBooking: StateFlow<BookingDetails> = _currentBooking.asStateFlow()

    fun confirmBooking(pickup: String, drop: String, fare: Int) {
        _currentBooking.value = BookingDetails(
            pickupAddress = pickup,
            destinationAddress = drop,
            fairPrice = fare
        )
    }

    // Overspeed incident tracking
    private val _overspeedAlert = MutableStateFlow<String?>(null)
    val overspeedAlert: StateFlow<String?> = _overspeedAlert.asStateFlow()

    fun triggerOverspeedIncident(driverName: String, speedKmh: Double) {
        val alertMsg = "Overspeed: $driverName at ${speedKmh.toInt()} km/h (Limit: 60 km/h)"
        _overspeedAlert.value = alertMsg
        _silentNotification.value = SilentPushNotification(
            id = "alert_${System.currentTimeMillis()}",
            message = "⚠️ OVERSPEED: $driverName at ${speedKmh.toInt()} km/h (Limit: 60 km/h)"
        )
    }

    fun dismissOverspeedAlert() {
        _overspeedAlert.value = null
    }

    // Subscriptions
    private val _isRentProSubscribed = MutableStateFlow(false)
    val isRentProSubscribed: StateFlow<Boolean> = _isRentProSubscribed.asStateFlow()

    private val _isHireDriverSubscribed = MutableStateFlow(false)
    val isHireDriverSubscribed: StateFlow<Boolean> = _isHireDriverSubscribed.asStateFlow()

    // Trigger silent push notification to admin within 7 seconds (NO SOUND, BADGE ONLY)
    fun scheduleSilentAdminNotification(formType: String, applicantName: String) {
        viewModelScope.launch {
            delay(7000L) // Exactly 7 seconds silent push
            _pendingVerificationCount.value += 1
            _silentNotification.value = SilentPushNotification(
                message = "New $formType profile submitted by $applicantName. Pending review: ${_pendingVerificationCount.value}",
                timestamp = "Just now"
            )
        }
    }

    fun dismissSilentNotification() {
        _silentNotification.value = null
    }

    // Submit Driver Profile
    fun submitDriverProfile(
        name: String,
        phone: String,
        vehicleType: String,
        dlNumber: String,
        rcNumber: String
    ) {
        scheduleSilentAdminNotification("Commercial Driver ($vehicleType)", name)
    }

    // Submit Rent Owner
    fun submitRentOwner(
        ownerName: String,
        companyName: String,
        vehicleCategory: String,
        vehicleModel: String,
        rcNumber: String
    ) {
        scheduleSilentAdminNotification("Rent Owner ($vehicleCategory - $vehicleModel)", ownerName)
    }

    // Submit Hire Driver (DL Only, No RC)
    fun submitHireDriver(
        name: String,
        phone: String,
        dlNumber: String,
        experience: String,
        drivingSkill: String
    ) {
        scheduleSilentAdminNotification("Hire Driver (DL Only - $experience)", name)
    }

    // Process Razorpay simulation
    fun processRazorpayPayment(
        amount: Int,
        description: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            delay(1200L) // Simulate instant payment gateway response
            if (description.contains("Rent", ignoreCase = true)) {
                _isRentProSubscribed.value = true
            } else if (description.contains("Driver", ignoreCase = true)) {
                _isHireDriverSubscribed.value = true
            } else {
                // Deduct from wallet or record payment
                _paymentHistory.value = listOf(
                    PaymentTransaction(
                        id = "tx_${System.currentTimeMillis()}",
                        title = description,
                        amount = amount,
                        method = "Razorpay / UPI",
                        timestamp = "Just now",
                        isSuccess = true
                    )
                ) + _paymentHistory.value
            }
            onSuccess()
        }
    }

    fun addMoneyToWallet(amount: Int) {
        _walletBalance.value += amount
        _paymentHistory.value = listOf(
            PaymentTransaction(
                id = "tx_${System.currentTimeMillis()}",
                title = "Added to Wallet",
                amount = amount,
                method = "UPI / Razorpay",
                timestamp = "Just now",
                isSuccess = true
            )
        ) + _paymentHistory.value
    }
}
