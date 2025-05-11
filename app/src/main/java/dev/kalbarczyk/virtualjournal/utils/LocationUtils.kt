package dev.kalbarczyk.virtualjournal.utils

import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.*
import kotlin.coroutines.resume

class LocationManager(
    private val context: Context, private val fusedLocationProviderClient: FusedLocationProviderClient
) {

    suspend fun getLocation(): Location? {
        val fineGranted = ContextCompat.checkSelfPermission(
            context, android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarseGranted = ContextCompat.checkSelfPermission(
            context, android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!fineGranted && !coarseGranted) {
            Log.w("LocationManager", "Location permission not granted")
            return null
        }

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled =
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
            )

        if (!isGpsEnabled) {
            Log.w("LocationManager", "Location provider is disabled")
            return null
        }

        return suspendCancellableCoroutine { cont ->
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        Log.d("LocationManager", "Got last known location: $location")
                        cont.resume(location)
                    } else {
                        Log.d("LocationManager", "Last location is null, requesting current location")
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            fusedLocationProviderClient.getCurrentLocation(
                                Priority.PRIORITY_HIGH_ACCURACY, CancellationTokenSource().token
                            ).addOnSuccessListener { currentLocation ->
                                Log.d("LocationManager", "Got current location: $currentLocation")
                                cont.resume(currentLocation)
                            }.addOnFailureListener { error ->
                                Log.e("LocationManager", "Failed to get current location", error)
                                cont.resume(null)
                            }
                        } else {
                            Log.w("LocationManager", "getCurrentLocation requires API 30+")
                            cont.resume(null)
                        }
                    }
                }.addOnFailureListener {
                    Log.e("LocationManager", "Failed to get last location", it)
                    cont.resume(null)
                }
        }
    }

    suspend fun getCityName(): String? {
        val location = getLocation()
        if (location == null) {
            Log.w("LocationManager", "Location is null, cannot resolve city")
            return "Unknown"
        }

        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val result = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            result?.firstOrNull()?.locality ?: "Unknown"
        } catch (e: Exception) {
            Log.e("LocationManager", "Geocoder failed", e)
            "Unknown"
        }
    }
}
