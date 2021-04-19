package com.phoqe.fackla.managers

import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.SystemClock
import com.mapbox.mapboxsdk.geometry.LatLng
import com.phoqe.fackla.services.FakeLocationNotificationService
import timber.log.Timber

class FakeLocationManager(private val context: Context) {
    private val testProviders = arrayOf(LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER)
    private val powerUsage = 1 // POWER_USAGE_LOW
    private val accuracy = 1 // ACCURACY_FINE
    private val intent = Intent(context, FakeLocationNotificationService::class.java)
    private val locMgr = context.getSystemService(Context.LOCATION_SERVICE) as
            LocationManager

    /**
     * Starts the fake location manager by creating test providers and fake locations to be set using
     * the created providers. The [point] is used when creating the fake location.
     */
    fun start(point: LatLng, callback: (Location) -> Unit) {
        Timber.v("start")

        setTestProviders()

        for (provider in testProviders) {
            locMgr.setTestProviderLocation(provider, createFakeLocation(provider, point))
            locMgr.setTestProviderEnabled(provider, true)
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }

        callback(createFakeLocation(testProviders.first(), point))
    }

    /**
     * Stops the fake location manager by disabling all test providers and removing them.
     */
    fun stop(callback: () -> Unit) {
        Timber.v("stop")

        for (provider in testProviders) {
            locMgr.setTestProviderEnabled(provider, false)
            locMgr.removeTestProvider(provider)
        }

        context.stopService(intent)

        callback()
    }

    /**
     * Sets up test providers for the fake location. The providers will be used to ultimately
     * set the fake location.
     */
    private fun setTestProviders() {
        Timber.v("addTestProvider")

        for (provider in testProviders) {
            locMgr.removeTestProvider(provider)

            locMgr.addTestProvider(
                    provider,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    powerUsage,
                    accuracy
            )
        }
    }

    /**
     * Creates a fake [Location] object from the provided [point]. It can be used when using
     * [LocationManager.setTestProviderLocation].
     */
    private fun createFakeLocation(provider: String, point: LatLng): Location {
        Timber.v("createFakeLocation")

        val loc = Location(provider)

        // Deduced from the point the user has clicked on the map.
        loc.latitude = point.latitude
        loc.longitude = point.longitude
        loc.altitude = point.altitude

        loc.time = System.currentTimeMillis()
        loc.accuracy = accuracy.toFloat()

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            loc.elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos()
        }

        Timber.d("Created fake location: $loc")

        return loc
    }
}