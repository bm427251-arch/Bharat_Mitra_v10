package com.example.model

enum class ScreenState {
    SPLASH,
    HOME,
    RENT_A_CAR,
    HIRE_DRIVER,
    ELITE_SERVICE,
    ADMIN_PANEL,
    ACTIVE_RIDE_TRACKING
}

enum class VehicleCategory(val displayName: String) {
    TOTO("Toto (E-Rickshaw)"),
    AUTO("Auto"),
    HATCHBACK("Hatchback"),
    SEDAN("Sedan"),
    SUV("SUV")
}

enum class VehicleVariant(val displayName: String) {
    NON_AC("Non-AC"),
    AC("AC"),
    PREMIUM("Premium")
}

data class RideOption(
    val id: String,
    val name: String,
    val category: String,
    val iconType: String,
    val hasAcOption: Boolean,
    val baseFareNonAc: Int,
    val baseFareAc: Int,
    val perKmRate: Double,
    val capacity: String,
    val etaMinutes: Int
)

data class DriverProfile(
    val id: String,
    val name: String,
    val avatarInitials: String,
    val phone: String,
    val rating: Double,
    val totalTrips: Int,
    val experienceYears: Int,
    val vehicleModel: String,
    val vehicleCategory: VehicleCategory,
    val rcNumber: String,
    val insuranceValidity: String,
    val isRcVerified: Boolean = true,
    val isInsuranceVerified: Boolean = true,
    val isAadhaarVerified: Boolean = true,
    val isCommercialDlVerified: Boolean = true,
    val isPoliceVerified: Boolean = true,
    val fixed8HrFee: Int = 800,
    val overtimePerHourRate: Int = 100,
    val bio: String,
    val languages: List<String> = listOf("Bengali", "Hindi", "English"),
    val badges: List<String> = listOf("Top Rated", "Safe Driver", "Background Verified")
)

data class RentalVehicle(
    val id: String,
    val title: String,
    val category: VehicleCategory,
    val variant: VehicleVariant,
    val ratePerHour: Int,
    val ratePerDay: Int,
    val seats: Int,
    val fuelType: String,
    val driver: DriverProfile
)

enum class EliteApprovalStatus {
    PENDING,
    APPROVED,
    REJECTED
}

data class EliteRegistration(
    val id: String,
    val fullName: String,
    val phone: String,
    val email: String,
    val maskedAadhaar: String, // Redacted Aadhaar e.g. XXXX-XXXX-4819
    val organizationName: String,
    val orgIdCardUri: String = "",
    val idCardFileName: String = "org_id_badge.jpg",
    val status: EliteApprovalStatus = EliteApprovalStatus.PENDING,
    val appliedDate: String = "Today, 10:30 AM",
    val notes: String = ""
)

enum class DotType {
    ORANGE_NEARBY_ELITE,
    GREEN_USER_GROUP
}

data class MapDot(
    val id: String,
    val name: String,
    val phone: String,
    val dotType: DotType,
    val latOffset: Float, // relative offset on interactive canvas
    val lngOffset: Float,
    val distanceKm: Double,
    val cityOrArea: String,
    val isOnline: Boolean = true,
    val lastPingSecondsAgo: Int = 12
)

enum class EmergencyType {
    ORANGE_PUBLIC, // Public Emergency: Alert sent to both Orange and Green dots
    GREEN_GROUP    // Group Only: Alert sent only to Green dots
}

data class EmergencyAlert(
    val id: String,
    val type: EmergencyType,
    val callerName: String,
    val callerPhone: String,
    val locationDescription: String,
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long = System.currentTimeMillis(),
    val isAudioStreaming: Boolean = true,
    val isVideoAcceptedByReceiver: Boolean = false, // Caller's camera remains OFF until receiver accepts
    val status: String = "ACTIVE"
)

data class LocationSuggestion(
    val title: String,
    val subtitle: String,
    val lat: Double,
    val lng: Double,
    val distanceKmFromCenter: Double
)

data class UserComplaint(
    val id: String,
    val userName: String,
    val userPhone: String,
    val issueType: String,
    val details: String,
    val filedAt: String,
    val status: ComplaintStatus = ComplaintStatus.PENDING,
    val adminReply: String = ""
)

enum class ComplaintStatus {
    PENDING,
    INVESTIGATING,
    RESOLVED
}

data class AdminMessage(
    val id: String,
    val title: String,
    val body: String,
    val sentAt: String,
    val recipientType: String = "All Users",
    val isUrgent: Boolean = false
)

data class UserRating(
    val id: String,
    val userName: String,
    val userPhone: String,
    val rating: Int,
    val feedback: String,
    val category: String,
    val date: String
)
