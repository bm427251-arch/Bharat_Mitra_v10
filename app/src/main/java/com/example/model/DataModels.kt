package com.example.model

enum class VehicleCategory(val displayName: String) {
    HATCHBACK("Hatchback"),
    SEDAN("Sedan"),
    SUV("SUV"),
    MUV("MUV"),
    LUXURY("Luxury"),
    EV("Electric Vehicle (EV)"),
    BIKE("Bike Taxi"),
    AUTO("Auto Rickshaw"),
    TOTO("Toto (E-Rickshaw)"),
    AMBULANCE("Ambulance"),
    LORRY("Lorry"),
    TRUCK("Truck"),
    PICKUP("Pickup Van"),
    BUS("Bus"),
    TRAVELLER("Traveller"),
    TEMPO("Tempo"),
    JCB("JCB"),
    CRANE("Crane"),
    OTHER("Other Commercial Vehicle")
}

data class FleetItem(
    val id: String,
    val name: String,
    val category: String,
    val availableCount: Int,
    val etaMinutes: Int,
    val price: Int = 100,
    val rating: Double = 4.8,
    val iconName: String = "car"
)

data class RentalCar(
    val id: String,
    val model: String,
    val category: String,
    val pricePerDay: Int,
    val seats: Int,
    val transmission: String,
    val fuelType: String,
    val isAvailable: Boolean = true,
    val location: String = "Kolkata / Barasat / Delhi NCR"
)

data class HireDriverItem(
    val id: String,
    val name: String,
    val rating: Double,
    val experienceYears: Int,
    val city: String,
    val ratePerHour: Int,
    val badge: String,
    val isVerified: Boolean = true,
    val distanceKm: Double = 2.3
)

data class PaymentTransaction(
    val id: String,
    val title: String,
    val amount: Int,
    val method: String, // "UPI", "Cash", "Wallet"
    val timestamp: String,
    val isSuccess: Boolean = true
)

data class BookingDetails(
    val bookingId: String = "BM-${System.currentTimeMillis() % 100000}",
    val pickupAddress: String = "Central Square, Stand No. 4",
    val destinationAddress: String = "Airport Terminal 2",
    val vehicleModel: String = "Toyota Sedan DL 01 AB 1234",
    val driverName: String = "Rohan Kumar",
    val driverRating: Double = 4.8,
    val etaMinutes: Int = 3,
    val distanceKm: Double = 2.1,
    val fairPrice: Int = 100
)

data class SilentPushNotification(
    val id: String = System.currentTimeMillis().toString(),
    val message: String,
    val timestamp: String = "Just now",
    val isRead: Boolean = false
)

data class NearbyLandmark(
    val id: String,
    val name: String,
    val category: String,
    val distance: String,
    val subtext: String
)

