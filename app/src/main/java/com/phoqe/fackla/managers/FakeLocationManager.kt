package com.phoqe.fackla.managers

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import androidx.preference.PreferenceManager
import com.mapbox.mapboxsdk.geometry.LatLng
import com.phoqe.fackla.events.FakeLocationManagerStartEvent
import com.phoqe.fackla.events.FakeLocationManagerStopEvent
import com.phoqe.fackla.services.FakeLocationNotificationService
import org.greenrobot.eventbus.EventBus
import timber.log.Timber

private const val POWER_USAGE = 1 // POWER_USAGE_LOW
private const val ACCURACY = 1 // ACCURACY_FINE
private const val PROVIDER_LOC_ENABLE_DELAY: Long = 1000

class FakeLocationManager(private val context: Context) {
    private val testProviders =
        arrayOf(LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER)
    private val intent = Intent(context, FakeLocationNotificationService::class.java)
    private val locMgr = context.getSystemService(Context.LOCATION_SERVICE) as
            LocationManager
    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)
    private val handler = Handler()

    private lateinit var runnable: Runnable

    var isActive = false

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: FakeLocationManager? = null

        /**
         * Presents a singleton version of the FakeLocationManager class. [context] is not leaked
         * even though the IDE says so.
         */
        fun getInstance(context: Context): FakeLocationManager {
            return when {
                instance != null -> instance!!
                else -> synchronized(this) {
                    if (instance == null) {
                        instance = FakeLocationManager(context)
                    }

                    instance!!
                }
            }
        }
    }

    /**
     * You can use this method to test whether Fackla can be used as a mock location app. The
     * function tests whether it can manage test providers, which is a qualifier for begin a mock
     * location app.
     */
    fun canManageTestProviders(): Boolean {
        Timber.v("isMockLocationApp")

        val provider = testProviders.random()

        Timber.d("Using provider: $provider")

        try {
            Timber.d("Testing add test provider...")

            addTestProvider(provider)
        } catch (ex: SecurityException) {
            Timber.e(ex, "Failed to add test provider.")

            return false
        }

        Timber.d("Could add test provider. Need to remove now.")

        try {
            Timber.d("Removing test provider...")

            locMgr.removeTestProvider(provider)
        } catch (ex: SecurityException) {
            Timber.e(ex, "Failed to remove test provider.")

            return false
        }

        Timber.d("Could remove test provider. Now the user is ready.")

        return true
    }

    /**
     * Performs a cold start using values from prefs. Useful when coming from a boot receiver or
     * booting the app during the winter (lol).
     */
    fun attemptStartFromPrefs() {
        Timber.v("attemptStartFromPrefs")

        if (!prefs.getBoolean("fake_loc_service_active", false)) {
            Timber.i("Fake Location Service wasn't active prior to boot.")

            return
        }

        Timber.d("Fake Location Service was running prior to boot.")

        val defaultCo = java.lang.Double.doubleToRawLongBits(0.0)
        val lat = java.lang.Double.longBitsToDouble(prefs.getLong("fake_lat", defaultCo))
        val long = java.lang.Double.longBitsToDouble(prefs.getLong("fake_long", defaultCo))
        val alt = java.lang.Double.longBitsToDouble(prefs.getLong("fake_alt", defaultCo))
        val point = LatLng(lat, long, alt)

        Timber.d("Using point: ${point}.")

        start(point)
    }

    /**
     * Starts the fake location manager by creating test providers and fake locations to be set using
     * the created providers. The [point] is used when creating the fake location.
     */
    fun start(point: LatLng, callback: ((Location) -> Unit)? = null) {
        Timber.v("start")

        runnable = Runnable {
            setTestProviders()

            for (provider in testProviders) {
                try {
                    locMgr.setTestProviderLocation(provider, createFakeLocation(provider, point))
                    locMgr.setTestProviderEnabled(provider, true)
                } catch (ex: IllegalArgumentException) {
                    Timber.e(ex, "Failed to set test provider location/enabled due to arguments.")

                    continue
                } catch (ex: SecurityException) {
                    Timber.e(
                        ex,
                        "Failed to set test provider location/enabled due to mock location ops."
                    )

                    stop()
                }
            }

            handler.postDelayed(runnable, PROVIDER_LOC_ENABLE_DELAY)
        }

        handler.post(runnable)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }

        val fakeLocation = createFakeLocation(testProviders.first(), point)

        isActive = true

        with(prefs.edit()) {
            putBoolean("fake_loc_service_active", true)

            val fakeLat = java.lang.Double.doubleToRawLongBits(point.latitude)
            val fakeLong = java.lang.Double.doubleToRawLongBits(point.longitude)
            val fakeAlt = java.lang.Double.doubleToRawLongBits(point.altitude)

            Timber.d(point.toString())
            Timber.d("fakeLat=$fakeLat;fakeLong=$fakeLong;fakeAlt=$fakeAlt")

            putLong("fake_lat", fakeLat)
            putLong("fake_long", fakeLong)
            putLong("fake_alt", fakeAlt)

            apply()
        }

        EventBus.getDefault().postSticky(FakeLocationManagerStartEvent(fakeLocation))

        callback?.let { it(fakeLocation) }
    }

    /**
     * Stops the fake location manager by disabling all test providers and removing them.
     */
    fun stop(callback: (() -> Unit)? = null) {
        Timber.v("stop")

        handler.removeCallbacks(runnable)

        for (provider in testProviders) {
            try {
                locMgr.removeTestProvider(provider)
            } catch (ex: IllegalArgumentException) {
                Timber.e(ex, "Failed to remove test provider due to args.")

                continue
            } catch (ex: SecurityException) {
                Timber.e(ex, "Failed to remove test provider due to mock location ops.")

                continue
            }
        }

        context.stopService(intent)

        isActive = false

        with(prefs.edit()) {
            remove("fake_loc_service_active")
            remove("fake_lat")
            remove("fake_long")
            remove("fake_alt")

            apply()
        }

        EventBus.getDefault().postSticky(FakeLocationManagerStopEvent())

        callback?.let { it() }
    }

    /**
     * Adds a test provider to the location manager.
     */
    private fun addTestProvider(provider: String) {
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

    /**
     * Sets up test providers for the fake location. The providers will be used to ultimately
     * set the fake location.
     */
    private fun setTestProviders() {
        Timber.v("addTestProvider")

        for (provider in testProviders) {
            try {
                addTestProvider(provider)
            } catch (ex: IllegalArgumentException) {
                Timber.e(ex, "Failed to add test provider due to args.")

                continue
            } catch (ex: SecurityException) {
                Timber.e(ex, "Failed to add test provider due to mock location ops.")

                stop()
            }
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