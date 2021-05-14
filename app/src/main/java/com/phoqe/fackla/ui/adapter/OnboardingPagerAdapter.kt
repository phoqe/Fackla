package com.phoqe.fackla.ui.adapter

import android.Manifest
import android.content.pm.PackageManager
import android.provider.Settings
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.phoqe.fackla.ui.activity.OnboardingActivity
import com.phoqe.fackla.ui.fragment.*
import timber.log.Timber

internal const val PAGER_ITEM_COUNT: Int = 5

class OnboardingPagerAdapter(private val activity: OnboardingActivity) :
    FragmentStateAdapter(activity) {
    private var isNewDev = false

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
        return PAGER_ITEM_COUNT
    }

    override fun createFragment(position: Int): Fragment {
        Timber.v("createFragment")
        Timber.d("Position: $position")
        Timber.d("Item Count: $itemCount")

        val introFragment = OnboardingIntroFragment()
        val devModeFragment = OnboardingDeveloperModeFragment()
        val mockLocationFragment = OnboardingMockLocationFragment()
        val endFragment = OnboardingEndFragment()
        val locPermFragment = OnboardingLocationPermissionFragment()

        introFragment.listener = activity
        devModeFragment.listener = activity
        mockLocationFragment.listener = activity
        endFragment.listener = activity
        locPermFragment.listener = activity

        return when (position) {
            0 -> introFragment
            1 -> {
                if (isDev()) {
                    mockLocationFragment
                } else {
                    isNewDev = true

                    devModeFragment
                }
            }
            2 -> {
                if (isNewDev) {
                    mockLocationFragment
                } else {
                    if (hasLocationPermission()) {
                        endFragment
                    } else {
                        locPermFragment
                    }
                }
            }
            3 -> {
                if (isNewDev) {
                    if (hasLocationPermission()) {
                        endFragment
                    } else {
                        locPermFragment
                    }
                } else {
                    endFragment
                }
            }
            4 -> endFragment
            else -> introFragment
        }
    }
}