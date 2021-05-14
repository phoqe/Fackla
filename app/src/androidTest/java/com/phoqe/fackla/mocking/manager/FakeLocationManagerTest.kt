package com.phoqe.fackla.mocking.manager

import android.location.LocationManager
import android.os.Looper
import androidx.preference.PreferenceManager
import androidx.test.platform.app.InstrumentationRegistry
import com.mapbox.mapboxsdk.geometry.LatLng
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import timber.log.Timber

class FakeLocationManagerTest {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val manager = FakeLocationManager.getInstance(context)
    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    private fun clearPrefs() {
        with(prefs.edit()) {
            clear()
            apply()
        }
    }

    private fun fillPrefs(point: LatLng) {
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
    }

    @Test
    fun startFromClearedPrefs() {
        clearPrefs()

        manager.attemptStartFromPrefs()

        assertFalse(manager.isActive)
    }

    @Test
    fun startFromFilledPrefs() {
        clearPrefs()

        val point = LatLng(26.82702, 142.72793)

        fillPrefs(point)

        manager.attemptStartFromPrefs()

        assertTrue(manager.isActive)
    }

    @Test
    fun start() {
        val point = LatLng(42.60896, -47.02990)

        manager.start(point)

        assertTrue(manager.isActive)
    }

    @Test
    fun createFakeLocationGps() {
        val provider = LocationManager.GPS_PROVIDER
        val point = LatLng(30.38004,  -47.76631)
        val loc = manager.createFakeLocation(provider, point)

        assertNotNull(loc)
        assertEquals(loc.provider, provider)
        assertEquals(loc.latitude, point.latitude, 0.0)
        assertEquals(loc.longitude, point.longitude, 0.0)
        assertEquals(loc.altitude, point.altitude, 0.0)
    }

    @Test
    fun createFakeLocationNetwork() {
        val provider = LocationManager.NETWORK_PROVIDER
        val point = LatLng(40.95261,  40.41502)
        val loc = manager.createFakeLocation(provider, point)

        assertNotNull(loc)
        assertEquals(loc.provider, provider)
        assertEquals(loc.latitude, point.latitude, 0.0)
        assertEquals(loc.longitude, point.longitude, 0.0)
        assertEquals(loc.altitude, point.altitude, 0.0)
    }
}