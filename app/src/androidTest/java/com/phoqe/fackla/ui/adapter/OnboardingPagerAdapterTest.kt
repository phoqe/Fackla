package com.phoqe.fackla.ui.adapter

import com.phoqe.fackla.ui.activity.OnboardingActivity
import com.phoqe.fackla.ui.fragment.*
import org.junit.Test

import org.junit.Assert.*

class OnboardingPagerAdapterTest {
    val activity = OnboardingActivity()
    val adapter = OnboardingPagerAdapter(activity)

    @Test
    fun normie() {
        assertEquals(adapter.createFragment(0), OnboardingIntroFragment::class.java)
        assertEquals(adapter.createFragment(1), OnboardingDeveloperModeFragment::class.java)
        assertEquals(adapter.createFragment(2), OnboardingMockLocationFragment::class.java)
        assertEquals(adapter.createFragment(3), OnboardingLocationPermissionFragment::class.java)
        assertEquals(adapter.createFragment(4), OnboardingEndFragment::class.java)
    }

    @Test
    fun normieLocPerms() {
        assertEquals(adapter.createFragment(0), OnboardingIntroFragment::class.java)
        assertEquals(adapter.createFragment(1), OnboardingDeveloperModeFragment::class.java)
        assertEquals(adapter.createFragment(2), OnboardingMockLocationFragment::class.java)
        assertEquals(adapter.createFragment(3), OnboardingEndFragment::class.java)
        assertEquals(adapter.createFragment(4), OnboardingEndFragment::class.java)
    }

    @Test
    fun dev() {
        assertEquals(adapter.createFragment(0), OnboardingIntroFragment::class.java)
        assertEquals(adapter.createFragment(1), OnboardingMockLocationFragment::class.java)
        assertEquals(adapter.createFragment(2), OnboardingLocationPermissionFragment::class.java)
        assertEquals(adapter.createFragment(3), OnboardingEndFragment::class.java)
        assertEquals(adapter.createFragment(4), OnboardingEndFragment::class.java)
    }

    @Test
    fun devLocPerms() {
        assertEquals(adapter.createFragment(0), OnboardingIntroFragment::class.java)
        assertEquals(adapter.createFragment(1), OnboardingMockLocationFragment::class.java)
        assertEquals(adapter.createFragment(2), OnboardingEndFragment::class.java)
        assertEquals(adapter.createFragment(3), OnboardingEndFragment::class.java)
        assertEquals(adapter.createFragment(4), OnboardingEndFragment::class.java)
    }
}