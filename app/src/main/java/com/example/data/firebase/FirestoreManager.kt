package com.example.data.firebase

import android.content.Context
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

// Data models with default parameters for Firestore deserialization
data class FirestoreUser(
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val aadhaarMasked: String = "",
    val role: String = "rider",
    val createdAt: String = ""
)

data class FirestoreEliteApp(
    val regName: String = "",
    val regPhone: String = "",
    val regEmail: String = "",
    val rawAadhaarDigits: String = "",
    val orgName: String = "",
    val isOrgIdUploaded: Boolean = false,
    val status: String = "PENDING",
    val appliedAt: String = ""
)

data class FirestoreComplaint(
    val userId: String = "",
    val description: String = "",
    val status: String = "PENDING",
    val adminReply: String = "",
    val createdAt: String = ""
)

data class FirestoreDriver(
    val name: String = "",
    val photoUrl: String = "",
    val rcNumber: String = "",
    val insuranceValidDate: String = "",
    val rating: Double = 4.9,
    val totalTrips: Int = 100,
    val isVerified: Boolean = true,
    val shiftFee: Int = 800,
    val overtimeRate: Int = 100
)

data class FirestoreRide(
    val pickupLat: Double = 22.7212,
    val pickupLng: Double = 88.4815,
    val dropLat: Double = 22.6540,
    val dropLng: Double = 88.4467,
    val fareBase: Int = 0,
    val fareWith15PercentCommission: Int = 0,
    val commissionAmount: Int = 0,
    val status: String = "CONFIRMED",
    val riderId: String = "user_rider_1",
    val driverId: String = "drv_1",
    val otp: String = "4819",
    val pickupAddress: String = "",
    val dropAddress: String = "",
    val createdAt: String = ""
)

data class FirestoreTariff(
    val bikeBase: Int = 30,
    val totoBase: Int = 50,
    val autoBase: Int = 80,
    val fourSeaterNonAc: Int = 180,
    val fourSeaterAc: Int = 240,
    val sevenSeaterNonAc: Int = 320,
    val sevenSeaterAc: Int = 390,
    val commissionPercent: Int = 15
)

object FirestoreManager {
    private const val TAG = "FirestoreManager"

    // Safe accessor for Firestore instance
    private fun getDb(context: Context): FirebaseFirestore? {
        return try {
            if (FirebaseApp.getApps(context).isEmpty()) {
                Log.w(TAG, "FirebaseApp is not initialized (no google-services.json)")
                null
            } else {
                FirebaseFirestore.getInstance()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error acquiring FirebaseFirestore: ${e.message}")
            null
        }
    }

    suspend fun saveRide(context: Context, ride: FirestoreRide): String? {
        val db = getDb(context) ?: return null
        return try {
            val docRef = db.collection("rides").add(ride).await()
            Log.d(TAG, "Ride stored successfully with ID: ${docRef.id}")
            docRef.id
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save ride: ${e.message}")
            null
        }
    }

    suspend fun loadTariffs(context: Context): FirestoreTariff {
        val defaultTariffs = FirestoreTariff()
        val db = getDb(context) ?: return defaultTariffs
        return try {
            val snapshot = db.collection("tariffs").document("standard_rates").get().await()
            if (snapshot.exists()) {
                snapshot.toObject(FirestoreTariff::class.java) ?: defaultTariffs
            } else {
                // Seed initial default tariffs
                db.collection("tariffs").document("standard_rates").set(defaultTariffs, SetOptions.merge())
                defaultTariffs
            }
        } catch (e: Exception) {
            Log.w(TAG, "Using fallback tariffs (Firestore query failed): ${e.message}")
            defaultTariffs
        }
    }

    suspend fun submitEliteApplication(context: Context, app: FirestoreEliteApp): Boolean {
        val db = getDb(context) ?: return true
        return try {
            db.collection("elite_applications").add(app).await()
            true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to submit elite application: ${e.message}")
            false
        }
    }

    suspend fun submitComplaint(context: Context, complaint: FirestoreComplaint): Boolean {
        val db = getDb(context) ?: return true
        return try {
            db.collection("complaints").add(complaint).await()
            true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to submit complaint: ${e.message}")
            false
        }
    }

    suspend fun saveUser(context: Context, user: FirestoreUser, userId: String): Boolean {
        val db = getDb(context) ?: return true
        return try {
            db.collection("users").document(userId).set(user, SetOptions.merge()).await()
            true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save user: ${e.message}")
            false
        }
    }
}
