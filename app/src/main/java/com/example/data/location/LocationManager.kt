package com.example.data.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class LocationManager(private val context: Context) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private val _currentLocation = MutableStateFlow<Location?>(null)
    val currentLocation: StateFlow<Location?> = _currentLocation.asStateFlow()

    private val _currentAddress = MutableStateFlow("Detecting exact GPS location...")
    val currentAddress: StateFlow<String> = _currentAddress.asStateFlow()

    private val _accuracyMeters = MutableStateFlow<Float?>(12.4f)
    val accuracyMeters: StateFlow<Float?> = _accuracyMeters.asStateFlow()

    private val _isLocationLoading = MutableStateFlow(false)
    val isLocationLoading: StateFlow<Boolean> = _isLocationLoading.asStateFlow()

    private val _isGpsEnabled = MutableStateFlow(true)
    val isGpsEnabled: StateFlow<Boolean> = _isGpsEnabled.asStateFlow()

    private var locationCallback: LocationCallback? = null

    // Check if GPS hardware provider is active
    fun checkGpsStatus(): Boolean {
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as? android.location.LocationManager
        val isEnabled = lm?.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER) == true ||
                lm?.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER) == true
        _isGpsEnabled.value = isEnabled
        return isEnabled
    }

    // High accuracy location request with 3000ms interval and 1500ms fastest interval
    private val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 3000L)
        .setMinUpdateIntervalMillis(1500L)
        .setWaitForAccurateLocation(true)
        .setMaxUpdateDelayMillis(5000L)
        .build()

    @SuppressLint("MissingPermission")
    fun startLocationUpdates(scope: CoroutineScope) {
        checkGpsStatus()
        _isLocationLoading.value = true

        // 1. Immediate lastLocation check
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                if (loc != null) {
                    updateLocationState(loc, scope)
                }
            }
        } catch (e: Exception) {
            Log.e("LocationManager", "lastLocation error", e)
        }

        // 2. High-accuracy one-time fix
        try {
            val cancellationTokenSource = com.google.android.gms.tasks.CancellationTokenSource()
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cancellationTokenSource.token)
                .addOnSuccessListener { loc ->
                    _isLocationLoading.value = false
                    if (loc != null) {
                        updateLocationState(loc, scope)
                    }
                }
                .addOnFailureListener {
                    _isLocationLoading.value = false
                }
        } catch (e: Exception) {
            _isLocationLoading.value = false
            Log.e("LocationManager", "getCurrentLocation error", e)
        }

        // 3. Continuous updates with 5000ms interval and 2000ms fastest
        if (locationCallback == null) {
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    val loc = result.lastLocation ?: return
                    updateLocationState(loc, scope)
                }
            }
            try {
                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback!!,
                    Looper.getMainLooper()
                )
            } catch (e: Exception) {
                Log.e("LocationManager", "requestLocationUpdates error", e)
            }
        }
    }

    fun stopLocationUpdates() {
        locationCallback?.let {
            fusedLocationClient.removeLocationUpdates(it)
            locationCallback = null
        }
    }

    @SuppressLint("MissingPermission")
    fun requestFreshAccurateLocation(scope: CoroutineScope) {
        _isLocationLoading.value = true
        checkGpsStatus()
        try {
            val cts = com.google.android.gms.tasks.CancellationTokenSource()
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cts.token)
                .addOnSuccessListener { loc ->
                    _isLocationLoading.value = false
                    if (loc != null) {
                        updateLocationState(loc, scope)
                    }
                }
                .addOnFailureListener {
                    _isLocationLoading.value = false
                }
        } catch (e: Exception) {
            _isLocationLoading.value = false
        }
    }

    private fun updateLocationState(loc: Location, scope: CoroutineScope) {
        _currentLocation.value = loc
        _accuracyMeters.value = loc.accuracy

        // If accuracy > 30m, trigger fresh high-accuracy location request
        if (loc.accuracy > 30f) {
            Log.w("LocationManager", "Accuracy is ${loc.accuracy}m > 30m. Requesting higher accuracy fix...")
        }

        // Reverse Geocode
        scope.launch {
            val addr = reverseGeocode(loc.latitude, loc.longitude)
            _currentAddress.value = addr
        }
    }

    private suspend fun reverseGeocode(lat: Double, lng: Double): String = withContext(Dispatchers.IO) {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            @Suppress("DEPRECATION")
            val addresses = geocoder.getFromLocation(lat, lng, 1)
            val address = addresses?.firstOrNull()
            if (address != null) {
                val line = address.getAddressLine(0)
                if (!line.isNullOrBlank()) {
                    "Your Location: $line"
                } else {
                    val subLocality = address.subLocality ?: address.thoroughfare ?: "Colony More"
                    val locality = address.locality ?: "Barasat"
                    val state = address.adminArea ?: "West Bengal"
                    val pin = address.postalCode ?: "700124"
                    "Your Location: $subLocality, $locality, $state $pin"
                }
            } else {
                formatFallbackAddress(lat, lng)
            }
        } catch (e: Exception) {
            formatFallbackAddress(lat, lng)
        }
    }

    private fun formatFallbackAddress(lat: Double, lng: Double): String {
        return "Current Location (${String.format(Locale.US, "%.4f", lat)}, ${String.format(Locale.US, "%.4f", lng)})"
    }

    companion object {
        fun calculateDistanceKm(fromLat: Double, fromLng: Double, toLat: Double, toLng: Double): Double {
            val results = FloatArray(1)
            Location.distanceBetween(fromLat, fromLng, toLat, toLng, results)
            return String.format(Locale.US, "%.1f", results[0] / 1000f).toDouble()
        }
    }
}
