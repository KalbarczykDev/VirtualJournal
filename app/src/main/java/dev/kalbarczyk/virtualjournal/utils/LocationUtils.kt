package dev.kalbarczyk.virtualjournal.utils

import android.Manifest
import android.content.Context
import android.location.Geocoder
import android.util.Log
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.tasks.await
import java.util.*

@RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
suspend fun getCityFromLocation(context: Context): String? {
    return try {
        val fusedLocationProvider = LocationServices.getFusedLocationProviderClient(context)

        val location = fusedLocationProvider.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            null
        ).await()

        if (location == null) {
            Log.w("Location", "Location is null – make sure emulator/device has a valid GPS location.")
            return null
        }

        val geocoder = Geocoder(context, Locale.getDefault())
        val result = geocoder.getFromLocation(location.latitude, location.longitude, 1)
        result?.firstOrNull()?.locality
    } catch (e: Exception) {
        Log.e("Location", "Error retrieving location or resolving city", e)
        null
    }
}
