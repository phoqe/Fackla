package com.phoqe.fackla

import com.mapbox.mapboxsdk.Mapbox
import org.junit.Test

import org.junit.Assert.*
import timber.log.Timber

class AppDelegateTest {
    @Test
    fun timberPlantedTree() {
        assertFalse(Timber.forest().isEmpty())
    }

    @Test
    fun mapboxInit() {
        assertTrue(Mapbox.hasInstance())
        assertNotNull(Mapbox.getAccessToken())
    }
}