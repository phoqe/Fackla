package com.phoqe.fackla

import com.mapbox.mapboxsdk.Mapbox
import org.junit.Test

import org.junit.Assert.*
import timber.log.Timber

class AppDelegateTest {
    private val appDelegate = AppDelegate()

    @Test
    fun onCreate() {
        // Make sure Timber is correctly initialized.
        assertFalse(Timber.forest().isEmpty())

        // Mapbox should be initialized.
        assertTrue(Mapbox.hasInstance())
        assertNotNull(Mapbox.getAccessToken())
    }
}