package com.example.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.SampleData
import com.example.data.firebase.*
import com.example.data.location.LocationManager as AppLocationManager
import com.example.model.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    // Navigation state - direct to HOME without logo splash
    private val _currentScreen = MutableStateFlow(ScreenState.HOME)
    val currentScreen: StateFlow<ScreenState> = _currentScreen.asStateFlow()

    // Back press tracker
    private var lastBackPressTime = 0L

    // Splash Admin long-press job (7 continuous seconds)
    private var adminLongPressJob: Job? = null
    private val _adminHoldTriggered = MutableStateFlow(false)
    val adminHoldTriggered: StateFlow<Boolean> = _adminHoldTriggered.asStateFlow()

    // Location & Booking state
    val appLocationManager = AppLocationManager(application)
    val currentLocation: StateFlow<Location?> = appLocationManager.currentLocation
    val accuracyMeters: StateFlow<Float?> = appLocationManager.accuracyMeters
    val isLocationLoading: StateFlow<Boolean> = appLocationManager.isLocationLoading
    val isGpsEnabled: StateFlow<Boolean> = appLocationManager.isGpsEnabled

    private val _pickupLocation = MutableStateFlow("Detecting exact GPS location...")
    val pickupLocation: StateFlow<String> = _pickupLocation.asStateFlow()

    private val _dynamicSuggestions = MutableStateFlow(SampleData.locationSuggestions)
    val dynamicSuggestions: StateFlow<List<LocationSuggestion>> = _dynamicSuggestions.asStateFlow()

    private val _dropLocation = MutableStateFlow("")
    val dropLocation: StateFlow<String> = _dropLocation.asStateFlow()

    private val _isDropConfirmed = MutableStateFlow(false)
    val isDropConfirmed: StateFlow<Boolean> = _isDropConfirmed.asStateFlow()

    private val _selectedRideOption = MutableStateFlow(SampleData.rideOptions[0])
    val selectedRideOption: StateFlow<RideOption> = _selectedRideOption.asStateFlow()

    private val _isAcSelected = MutableStateFlow(false)
    val isAcSelected: StateFlow<Boolean> = _isAcSelected.asStateFlow()

    private val _calculatedFare = MutableStateFlow(30)
    val calculatedFare: StateFlow<Int> = _calculatedFare.asStateFlow()

    private val _calculatedFareWithCommission = MutableStateFlow(35)
    val calculatedFareWithCommission: StateFlow<Int> = _calculatedFareWithCommission.asStateFlow()

    private val _platformCommissionAmount = MutableStateFlow(5)
    val platformCommissionAmount: StateFlow<Int> = _platformCommissionAmount.asStateFlow()

    private val _currentTariffs = MutableStateFlow(FirestoreTariff())
    val currentTariffs: StateFlow<FirestoreTariff> = _currentTariffs.asStateFlow()

    private val _estimatedDistanceKm = MutableStateFlow(3.8)
    val estimatedDistanceKm: StateFlow<Double> = _estimatedDistanceKm.asStateFlow()

    // Active Ride state
    private val _activeRideDriver = MutableStateFlow<DriverProfile?>(null)
    val activeRideDriver: StateFlow<DriverProfile?> = _activeRideDriver.asStateFlow()

    private val _rideOtp = MutableStateFlow("4821")
    val rideOtp: StateFlow<String> = _rideOtp.asStateFlow()

    // Rent A Car state
    private val _selectedRentCategory = MutableStateFlow(VehicleCategory.TOTO)
    val selectedRentCategory: StateFlow<VehicleCategory> = _selectedRentCategory.asStateFlow()

    private val _selectedRentVariant = MutableStateFlow(VehicleVariant.AC)
    val selectedRentVariant: StateFlow<VehicleVariant> = _selectedRentVariant.asStateFlow()

    // Driver Profile Wall Dialog state
    private val _profileWallDriver = MutableStateFlow<DriverProfile?>(null)
    val profileWallDriver: StateFlow<DriverProfile?> = _profileWallDriver.asStateFlow()

    private val _profileWallRentalInfo = MutableStateFlow<Pair<String, String>?>(null)
    val profileWallRentalInfo: StateFlow<Pair<String, String>?> = _profileWallRentalInfo.asStateFlow()

    // Elite Section state
    private val _isEliteSubscribed = MutableStateFlow(false)
    val isEliteSubscribed: StateFlow<Boolean> = _isEliteSubscribed.asStateFlow()

    private val _eliteRadiusKm = MutableStateFlow(3)
    val eliteRadiusKm: StateFlow<Int> = _eliteRadiusKm.asStateFlow()

    private val _mapDots = MutableStateFlow(SampleData.mapDots)
    val mapDots: StateFlow<List<MapDot>> = _mapDots.asStateFlow()

    private val _eliteRegistrations = MutableStateFlow(SampleData.sampleEliteRegistrations)
    val eliteRegistrations: StateFlow<List<EliteRegistration>> = _eliteRegistrations.asStateFlow()

    private val _is24HrGpsSharingActive = MutableStateFlow(false)
    val is24HrGpsSharingActive: StateFlow<Boolean> = _is24HrGpsSharingActive.asStateFlow()

    // Active Emergency SOS Alert
    private val _activeEmergencyAlert = MutableStateFlow<EmergencyAlert?>(null)
    val activeEmergencyAlert: StateFlow<EmergencyAlert?> = _activeEmergencyAlert.asStateFlow()

    // List of verified drivers (editable by admin)
    private val _verifiedDrivers = MutableStateFlow(SampleData.verifiedDrivers)
    val verifiedDrivers: StateFlow<List<DriverProfile>> = _verifiedDrivers.asStateFlow()

    // Rental fleets
    private val _rentalVehicles = MutableStateFlow(SampleData.rentalVehicles)
    val rentalVehicles: StateFlow<List<RentalVehicle>> = _rentalVehicles.asStateFlow()

    // Dynamic Logo state (Admin uploaded custom PNG/JPG or null for default vector)
    private val _customLogoUri = MutableStateFlow<String?>(null)
    val customLogoUri: StateFlow<String?> = _customLogoUri.asStateFlow()

    // Admin App Design & Announcement Customization
    private val _appAnnouncement = MutableStateFlow("Bharat Mitra Official • Bengal's Premier Community Transport & Safety Network")
    val appAnnouncement: StateFlow<String> = _appAnnouncement.asStateFlow()

    // User Complaints Desk
    private val _complaints = MutableStateFlow(SampleData.sampleComplaints)
    val complaints: StateFlow<List<UserComplaint>> = _complaints.asStateFlow()

    // User Ratings & Reviews
    private val _userRatings = MutableStateFlow(SampleData.sampleRatings)
    val userRatings: StateFlow<List<UserRating>> = _userRatings.asStateFlow()

    // Admin Direct Messages & Broadcasts
    private val _adminMessages = MutableStateFlow(SampleData.sampleAdminMessages)
    val adminMessages: StateFlow<List<AdminMessage>> = _adminMessages.asStateFlow()

    // Category Profile Creation State (Rider, Driver, Rent A Car)
    private val _riders = MutableStateFlow(SampleData.sampleRiders)
    val riders: StateFlow<List<RiderProfile>> = _riders.asStateFlow()

    private val _rentOwners = MutableStateFlow(SampleData.sampleRentOwners)
    val rentOwners: StateFlow<List<RentACarOwnerProfile>> = _rentOwners.asStateFlow()

    private val _selectedRole = MutableStateFlow("RIDER")
    val selectedRole: StateFlow<String> = _selectedRole.asStateFlow()

    private val _selectedVehicleType = MutableStateFlow("BIKE")
    val selectedVehicleType: StateFlow<String> = _selectedVehicleType.asStateFlow()

    fun setSelectedCategory(role: String, vehicleType: String = "") {
        _selectedRole.value = role
        if (vehicleType.isNotBlank()) {
            _selectedVehicleType.value = vehicleType
        }
    }

    fun registerRider(name: String, phone: String, vehicleType: String, vehicleNumber: String, address: String, dl: String) {
        val newRider = RiderProfile(
            id = "rdr_${System.currentTimeMillis()}",
            name = name,
            phone = phone,
            vehicleType = vehicleType,
            vehicleNumber = vehicleNumber,
            address = address,
            drivingLicence = dl.ifBlank { if (vehicleType == "TOTO") "Exempt / Verified e-Vehicle" else "Under Verification" },
            rating = 5.0,
            isVerified = true
        )
        _riders.value = listOf(newRider) + _riders.value
        Toast.makeText(getApplication(), "Rider profile registered successfully!", Toast.LENGTH_SHORT).show()
    }

    fun registerDriver(
        name: String,
        phone: String,
        vehicleModel: String,
        vehicleNumber: String,
        vehicleCategory: VehicleCategory,
        dl: String,
        rc: String,
        insurance: String
    ) {
        val initials = name.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString("").uppercase()
        val newDriver = DriverProfile(
            id = "drv_${System.currentTimeMillis()}",
            name = name,
            avatarInitials = if (initials.isNotBlank()) initials else "DR",
            phone = phone,
            rating = 5.0,
            totalTrips = 0,
            experienceYears = 3,
            vehicleModel = vehicleModel,
            vehicleCategory = vehicleCategory,
            rcNumber = rc,
            insuranceValidity = insurance,
            isRcVerified = true,
            isInsuranceVerified = true,
            isAadhaarVerified = true,
            isCommercialDlVerified = true,
            isPoliceVerified = true,
            fixed8HrFee = 800,
            overtimePerHourRate = 100,
            bio = "Verified professional driver on Bharat Mitra Network.",
            badges = listOf("Police Verified", "Commercial DL", "New Driver")
        )
        _verifiedDrivers.value = listOf(newDriver) + _verifiedDrivers.value
        Toast.makeText(getApplication(), "Driver profile registered successfully!", Toast.LENGTH_SHORT).show()
    }

    fun registerRentOwner(
        ownerName: String,
        companyName: String,
        phone: String,
        address: String,
        pan: String,
        licence: String,
        bank: String,
        cars: List<RentCarItem>
    ) {
        val newOwner = RentACarOwnerProfile(
            id = "rent_own_${System.currentTimeMillis()}",
            ownerName = ownerName,
            companyName = companyName,
            phone = phone,
            address = address,
            panNumber = pan,
            businessLicence = licence,
            bankDetails = bank,
            cars = cars,
            isVerified = true
        )
        _rentOwners.value = listOf(newOwner) + _rentOwners.value
        Toast.makeText(getApplication(), "Rent a Car business profile registered!", Toast.LENGTH_SHORT).show()
    }

    init {
        updateCalculatedFare()
        loadFirestoreTariffs()

        // Sync real-time location address to pickup location
        viewModelScope.launch {
            appLocationManager.currentAddress.collect { addr ->
                _pickupLocation.value = addr
            }
        }

        // Sync location to dynamic suggestions distance
        viewModelScope.launch {
            appLocationManager.currentLocation.collect { loc ->
                updateDynamicSuggestions(loc)
            }
        }
    }

    fun startLocationUpdates() {
        appLocationManager.startLocationUpdates(viewModelScope)
    }

    fun refreshAccurateLocation() {
        appLocationManager.requestFreshAccurateLocation(viewModelScope)
    }

    fun checkGpsStatus(): Boolean {
        return appLocationManager.checkGpsStatus()
    }

    private fun updateDynamicSuggestions(currentLoc: Location?) {
        if (currentLoc == null) {
            _dynamicSuggestions.value = SampleData.locationSuggestions
            return
        }
        _dynamicSuggestions.value = SampleData.locationSuggestions.map { sug ->
            val dist = AppLocationManager.calculateDistanceKm(
                currentLoc.latitude,
                currentLoc.longitude,
                sug.lat,
                sug.lng
            )
            sug.copy(dynamicDistanceKm = dist)
        }.sortedBy { it.dynamicDistanceKm ?: it.distanceKmFromCenter }
    }

    private fun loadFirestoreTariffs() {
        viewModelScope.launch {
            val tariffs = FirestoreManager.loadTariffs(getApplication())
            _currentTariffs.value = tariffs
            Log.d("MainViewModel", "Loaded Firestore tariffs (Commission: ${tariffs.commissionPercent}%)")
        }
    }

    // Navigation Methods
    fun navigateTo(screen: ScreenState) {
        _currentScreen.value = screen
    }

    /**
     * Requirement 6: App Navigation & Back Button Handling
     * - Pressing back should bring user back to Home page instead of exiting.
     * - Double pressing back on Home page shows: "Press back again to exit"
     */
    fun handleBackPress(onExitApp: () -> Unit) {
        if (_currentScreen.value != ScreenState.HOME) {
            _currentScreen.value = ScreenState.HOME
        } else {
            val now = System.currentTimeMillis()
            if (now - lastBackPressTime < 2000) {
                onExitApp()
            } else {
                lastBackPressTime = now
                Toast.makeText(getApplication(), "Press back again to exit", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Splash Admin Long Press: Exactly 7 continuous seconds
    fun startAdminLongPress() {
        adminLongPressJob?.cancel()
        adminLongPressJob = viewModelScope.launch {
            delay(7000) // 7 continuous seconds without counter
            triggerVibration()
            _adminHoldTriggered.value = true
            _currentScreen.value = ScreenState.ADMIN_PANEL
            Toast.makeText(getApplication(), "Admin Panel Unlocked (7-sec security bypass)", Toast.LENGTH_LONG).show()
        }
    }

    fun cancelAdminLongPress() {
        adminLongPressJob?.cancel()
        adminLongPressJob = null
    }

    private fun triggerVibration() {
        try {
            val vibrator = getApplication<Application>().getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(120, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(120)
            }
        } catch (_: Exception) {}
    }

    // GPS Auto-detect Pickup
    fun autoDetectPickupLocation() {
        refreshAccurateLocation()
        Toast.makeText(getApplication(), "Recalibrating high-accuracy GPS...", Toast.LENGTH_SHORT).show()
    }

    fun setPickupLocation(pickup: String) {
        _pickupLocation.value = pickup
    }

    fun setDropLocation(drop: String) {
        _dropLocation.value = drop
        // Auto-calculate dynamic road distance from current GPS coordinates
        val matched = _dynamicSuggestions.value.find { it.title.equals(drop, ignoreCase = true) }
        val curLoc = currentLocation.value
        val dist = if (matched != null && curLoc != null) {
            AppLocationManager.calculateDistanceKm(
                curLoc.latitude,
                curLoc.longitude,
                matched.lat,
                matched.lng
            )
        } else {
            matched?.dynamicDistanceKm ?: matched?.distanceKmFromCenter ?: (3.0 + (drop.length % 7))
        }
        _estimatedDistanceKm.value = dist
        updateCalculatedFare()
    }

    fun confirmDropLocation() {
        if (_dropLocation.value.isNotBlank()) {
            _isDropConfirmed.value = true
            updateCalculatedFare()
        } else {
            Toast.makeText(getApplication(), "Please select or type a drop location first", Toast.LENGTH_SHORT).show()
        }
    }

    fun resetDropLocation() {
        _isDropConfirmed.value = false
        _dropLocation.value = ""
    }

    fun selectRideOption(option: RideOption) {
        _selectedRideOption.value = option
        if (!option.hasAcOption) {
            _isAcSelected.value = false
        }
        updateCalculatedFare()
    }

    fun toggleAc(enable: Boolean) {
        _isAcSelected.value = enable
        updateCalculatedFare()
    }

    private fun updateCalculatedFare() {
        val option = _selectedRideOption.value
        val dist = _estimatedDistanceKm.value
        val baseFare = if (_isAcSelected.value && option.hasAcOption) option.baseFareAc else option.baseFareNonAc
        val extraDistFare = (dist * option.perKmRate).toInt()
        val calculatedBase = baseFare + (extraDistFare / 3)
        _calculatedFare.value = calculatedBase

        // 15% Platform commission calculation
        val commissionRate = _currentTariffs.value.commissionPercent / 100.0
        val withCommission = (calculatedBase * (1.0 + commissionRate)).toInt()
        _calculatedFareWithCommission.value = withCommission
        _platformCommissionAmount.value = withCommission - calculatedBase
    }

    fun bookCurrentRide() {
        val assignedDriver = _verifiedDrivers.value.randomOrNull() ?: SampleData.verifiedDrivers[0]
        _activeRideDriver.value = assignedDriver
        val generatedOtp = (1000..9999).random().toString()
        _rideOtp.value = generatedOtp
        _currentScreen.value = ScreenState.ACTIVE_RIDE_TRACKING
        triggerVibration()
        Toast.makeText(getApplication(), "Driver Assigned! ${assignedDriver.name} is arriving in 3 mins.", Toast.LENGTH_LONG).show()

        // Asynchronously save ride to Firestore rides collection
        viewModelScope.launch {
            val base = _calculatedFare.value
            val withCommission = _calculatedFareWithCommission.value
            val commission = _platformCommissionAmount.value
            val timestamp = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())

            FirestoreManager.saveRide(
                getApplication(),
                FirestoreRide(
                    pickupAddress = _pickupLocation.value,
                    dropAddress = _dropLocation.value,
                    fareBase = base,
                    fareWith15PercentCommission = withCommission,
                    commissionAmount = commission,
                    status = "DRIVER_ASSIGNED",
                    riderId = "user_app_rider",
                    driverId = assignedDriver.id,
                    otp = generatedOtp,
                    createdAt = timestamp
                )
            )
        }
    }

    fun cancelActiveRide() {
        _activeRideDriver.value = null
        _currentScreen.value = ScreenState.HOME
        Toast.makeText(getApplication(), "Ride cancelled", Toast.LENGTH_SHORT).show()
    }

    // Rent A Car Filter Logic
    fun selectRentCategory(category: VehicleCategory) {
        _selectedRentCategory.value = category
    }

    fun selectRentVariant(variant: VehicleVariant) {
        _selectedRentVariant.value = variant
    }

    // Driver Profile Wall actions
    fun openDriverProfileWall(driver: DriverProfile, rentalTitle: String? = null, rentalPrice: String? = null) {
        _profileWallDriver.value = driver
        _profileWallRentalInfo.value = if (rentalTitle != null && rentalPrice != null) Pair(rentalTitle, rentalPrice) else null
    }

    fun closeDriverProfileWall() {
        _profileWallDriver.value = null
        _profileWallRentalInfo.value = null
    }

    // Elite Section Logic
    fun setEliteRadius(radiusKm: Int) {
        _eliteRadiusKm.value = radiusKm
    }

    fun submitEliteRegistration(name: String, phone: String, email: String, aadhaar: String, orgName: String) {
        // Redact Aadhaar to XXXX-XXXX-Last4
        val digitsOnly = aadhaar.filter { it.isDigit() }
        val last4 = if (digitsOnly.length >= 4) digitsOnly.takeLast(4) else "4819"
        val maskedAadhaar = "XXXX-XXXX-$last4"

        val newRegistration = EliteRegistration(
            id = "reg_${System.currentTimeMillis()}",
            fullName = name,
            phone = phone,
            email = email,
            maskedAadhaar = maskedAadhaar,
            organizationName = orgName,
            status = EliteApprovalStatus.PENDING,
            appliedDate = "Just now",
            notes = "Political / Organizational ID Card attached for scrutiny."
        )

        _eliteRegistrations.value = listOf(newRegistration) + _eliteRegistrations.value
        Toast.makeText(
            getApplication(),
            "Registration submitted! Awaiting Admin verification for Elite badge.",
            Toast.LENGTH_LONG
        ).show()

        // Sync to Firestore elite_applications collection
        viewModelScope.launch {
            FirestoreManager.submitEliteApplication(
                getApplication(),
                FirestoreEliteApp(
                    regName = name,
                    regPhone = phone,
                    regEmail = email,
                    rawAadhaarDigits = maskedAadhaar,
                    orgName = orgName,
                    isOrgIdUploaded = true,
                    status = "PENDING",
                    appliedAt = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())
                )
            )
        }
    }

    // Razorpay Integration
    fun openRazorpayPayment(context: Context) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(SampleData.RAZORPAY_PAYMENT_URL))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Could not open browser. Link: ${SampleData.RAZORPAY_PAYMENT_URL}", Toast.LENGTH_LONG).show()
        }
    }

    fun toggle24HrGpsSharing(enable: Boolean, context: Context) {
        if (enable) {
            // Prompt Razorpay fee
            openRazorpayPayment(context)
            _is24HrGpsSharingActive.value = true
            Toast.makeText(context, "24-Hour Continuous Group GPS Sharing Activated", Toast.LENGTH_LONG).show()
        } else {
            _is24HrGpsSharingActive.value = false
            Toast.makeText(context, "Continuous Group GPS Sharing deactivated", Toast.LENGTH_SHORT).show()
        }
    }

    // Emergency SOS Calls
    fun triggerOrangeCall() {
        val alert = EmergencyAlert(
            id = "sos_${System.currentTimeMillis()}",
            type = EmergencyType.ORANGE_PUBLIC,
            callerName = "You (Elite Member)",
            callerPhone = "+91 98301 00000",
            locationDescription = _pickupLocation.value,
            latitude = 22.7212,
            longitude = 88.4815,
            isVideoAcceptedByReceiver = false
        )
        _activeEmergencyAlert.value = alert
        triggerVibration()
    }

    fun triggerGreenCall() {
        val alert = EmergencyAlert(
            id = "sos_${System.currentTimeMillis()}",
            type = EmergencyType.GREEN_GROUP,
            callerName = "You (Family Group)",
            callerPhone = "+91 98301 00000",
            locationDescription = _pickupLocation.value,
            latitude = 22.7212,
            longitude = 88.4815,
            isVideoAcceptedByReceiver = false
        )
        _activeEmergencyAlert.value = alert
        triggerVibration()
    }

    fun acceptVideoByReceiver() {
        _activeEmergencyAlert.value = _activeEmergencyAlert.value?.copy(isVideoAcceptedByReceiver = true)
        Toast.makeText(getApplication(), "Receiver accepted video call! Camera stream now active.", Toast.LENGTH_SHORT).show()
    }

    fun endEmergencyCall() {
        _activeEmergencyAlert.value = null
        Toast.makeText(getApplication(), "Emergency SOS broadcast terminated", Toast.LENGTH_SHORT).show()
    }

    // Admin Panel Actions
    fun approveEliteRegistration(id: String) {
        _eliteRegistrations.value = _eliteRegistrations.value.map {
            if (it.id == id) it.copy(status = EliteApprovalStatus.APPROVED) else it
        }
        _isEliteSubscribed.value = true
        Toast.makeText(getApplication(), "Member approved! Elite protection badge activated.", Toast.LENGTH_SHORT).show()
    }

    fun rejectEliteRegistration(id: String) {
        _eliteRegistrations.value = _eliteRegistrations.value.map {
            if (it.id == id) it.copy(status = EliteApprovalStatus.REJECTED) else it
        }
        Toast.makeText(getApplication(), "Member application rejected", Toast.LENGTH_SHORT).show()
    }

    fun updateDriverTariff(driverId: String, fixedFee: Int, overtimeRate: Int) {
        _verifiedDrivers.value = _verifiedDrivers.value.map {
            if (it.id == driverId) it.copy(fixed8HrFee = fixedFee, overtimePerHourRate = overtimeRate) else it
        }
        Toast.makeText(getApplication(), "Driver tariff updated successfully", Toast.LENGTH_SHORT).show()
    }

    // Dynamic Logo Upload & Reset
    fun uploadNewLogo(uri: String) {
        _customLogoUri.value = uri
        Toast.makeText(getApplication(), "Logo updated successfully across the entire application!", Toast.LENGTH_LONG).show()
    }

    fun resetLogoToDefault() {
        _customLogoUri.value = null
        Toast.makeText(getApplication(), "Reset to default high-precision vector-coded logo", Toast.LENGTH_SHORT).show()
    }

    // App Design & Announcement Customization
    fun updateAppAnnouncement(banner: String) {
        _appAnnouncement.value = banner
        Toast.makeText(getApplication(), "App announcement banner updated!", Toast.LENGTH_SHORT).show()
    }

    // Complaints Desk
    fun resolveComplaint(complaintId: String, adminReply: String) {
        _complaints.value = _complaints.value.map {
            if (it.id == complaintId) it.copy(status = ComplaintStatus.RESOLVED, adminReply = adminReply) else it
        }
        Toast.makeText(getApplication(), "Complaint marked as Resolved & reply sent to user", Toast.LENGTH_SHORT).show()
    }

    fun submitUserComplaint(issueType: String, details: String) {
        val newComplaint = UserComplaint(
            id = "cmp_${System.currentTimeMillis()}",
            userName = "Current User",
            userPhone = "+91 98301 55678",
            issueType = issueType,
            details = details,
            filedAt = "Just now",
            status = ComplaintStatus.PENDING,
            adminReply = ""
        )
        _complaints.value = listOf(newComplaint) + _complaints.value
        Toast.makeText(getApplication(), "Complaint submitted. Admin desk will review shortly.", Toast.LENGTH_LONG).show()
    }

    // Admin Direct Messaging & Broadcast
    fun sendAdminDirectMessage(title: String, body: String, isUrgent: Boolean) {
        val newMsg = AdminMessage(
            id = "msg_${System.currentTimeMillis()}",
            title = title,
            body = body,
            sentAt = "Just now",
            recipientType = "All Active Users",
            isUrgent = isUrgent
        )
        _adminMessages.value = listOf(newMsg) + _adminMessages.value
        triggerVibration()
        Toast.makeText(getApplication(), "Broadcast message transmitted to all users!", Toast.LENGTH_LONG).show()
    }
}
