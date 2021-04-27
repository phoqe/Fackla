package com.phoqe.fackla.adapters

import android.Manifest
import android.content.pm.PackageManager
import android.provider.Settings
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.phoqe.fackla.activities.OnboardingActivity
import com.phoqe.fackla.fragments.*

private const val NUM_PAGES = 5

class OnboardingPagerAdapter(private val activity: OnboardingActivity, private val viewPager: ViewPager2) : FragmentStateAdapter(activity) {
    private val isNewDev = false

    private fun isDev(): Boolean {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Settings.Secure.getInt(
                    activity.contentResolver,
                    Settings.Global.DEVELOPMENT_SETTINGS_ENABLED,
                    0
            ) == 1
        } else {
            Settings.Secure.getInt(
                    activity.contentResolver,
                    Settings.Secure.DEVELOPMENT_SETTINGS_ENABLED,
                    0
            ) == 1
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun getItemCount(): Int {
        return NUM_PAGES
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OnboardingIntroFragment()
            1 -> OnboardingDeveloperModeFragment()
            2 -> OnboardingMockLocationFragment()
            3 -> OnboardingLocationPermissionFragment()
            4 -> OnboardingEndFragment(activity)
            else -> OnboardingIntroFragment()
        }
    }
}