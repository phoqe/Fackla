package com.phoqe.fackla.managers

import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.SystemClock
import com.mapbox.mapboxsdk.geometry.LatLng
import timber.log.Timber

class FakeLocationManager(val context: Context) {
    private val TEST_PROVIDERS = arrayOf(LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER)
    private val POWER_USAGE = 1 // `POWER_USAGE_LOW`
    private val ACCURACY = 1 // `ACCURACY_FINE`

    private val locMgr = context.getSystemService(Context.LOCATION_SERVICE) as
            LocationManager

    /**
     * Starts the fake location manager by creating test providers and fake locations to be set using
     * the created providers. The [point] is used when creating the fake location.
     */
    fun start(point: LatLng, callback: (Location) -> Unit) {
        Timber.v("start")

        setTestProviders()

        for (provider in TEST_PROVIDERS) {
            locMgr.setTestProviderLocation(provider, createFakeLocation(provider, point))
            locMgr.setTestProviderEnabled(provider, true)
        }

        callback(createFakeLocation(TEST_PROVIDERS.first(), point))
    }

    /**
     * Stops the fake location manager by disabling all test providers and removing them.
     */
    fun stop(callback: () -> Unit) {
        Timber.v("stop")

        for (provider in TEST_PROVIDERS) {
            locMgr.setTestProviderEnabled(provider, false)
            locMgr.removeTestProvider(provider)
        }

        callback()
    }

    /**
     * Sets up test providers for the fake location. The providers will be used to ultimately
     * set the fake location.
     */
    private fun setTestProviders() {
        Timber.v("addTestProvider")

        for (provider in TEST_PROVIDERS) {
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
                    POWER_USAGE,
                    ACCURACY
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
        loc.accuracy = ACCURACY.toFloat()

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            loc.elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos()
        }

        Timber.d("Created fake location: $loc")

        return loc
    }
}