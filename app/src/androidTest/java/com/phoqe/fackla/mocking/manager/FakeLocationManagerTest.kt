package com.phoqe.fackla.mocking.manager

import android.content.Context
import android.location.LocationManager
import androidx.preference.PreferenceManager
import androidx.test.platform.app.InstrumentationRegistry
import com.mapbox.mapboxsdk.geometry.LatLng
import org.junit.Assert.*
import org.junit.Test
import timber.log.Timber

private val TEST_POINTS = arrayOf(
    LatLng(42.60896, -47.02990),
    LatLng(26.82702, 142.72793),
    LatLng(30.38004,  -47.76631),
    LatLng(40.95261,  40.41502)
)

class FakeLocationManagerTest {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val fakeLocMgr = FakeLocationManager.getInstance(context)
    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)
    private val locMgr = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

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

        fakeLocMgr.attemptStartFromPrefs()

        assertFalse(fakeLocMgr.isActive)
    }

    @Test
    fun startFromFilledPrefs() {
        clearPrefs()

        val point = TEST_POINTS.random()

        fillPrefs(point)

        fakeLocMgr.attemptStartFromPrefs()

        assertTrue(fakeLocMgr.isActive)
    }

    @Test
    fun start() {
        val point = TEST_POINTS.random()

        fakeLocMgr.start(point)

        assertTrue(fakeLocMgr.isActive)
    }

    @Test
    fun stop() {
        fakeLocMgr.stop()

        assertFalse(fakeLocMgr.isActive)
    }

    @Test
    fun startStop() {
        start()
        stop()
    }

    @Test
    fun addTestProviderGps() {
        fakeLocMgr.addTestProvider(LocationManager.GPS_PROVIDER)

        assertTrue(locMgr.allProviders.contains(LocationManager.GPS_PROVIDER))
    }

    @Test
    fun createFakeLocationGps() {
        val provider = LocationManager.GPS_PROVIDER
        val point = TEST_POINTS.random()
        val loc = fakeLocMgr.createFakeLocation(provider, point)

        assertNotNull(loc)
        assertEquals(loc.provider, provider)
        assertEquals(loc.latitude, point.latitude, 0.0)
        assertEquals(loc.longitude, point.longitude, 0.0)
        assertEquals(loc.altitude, point.altitude, 0.0)
    }

    @Test
    fun createFakeLocationNetwork() {
        val provider = LocationManager.NETWORK_PROVIDER
        val point = TEST_POINTS.random()
        val loc = fakeLocMgr.createFakeLocation(provider, point)

        assertNotNull(loc)
        assertEquals(loc.provider, provider)
        assertEquals(loc.latitude, point.latitude, 0.0)
        assertEquals(loc.longitude, point.longitude, 0.0)
        assertEquals(loc.altitude, point.altitude, 0.0)
    }
}