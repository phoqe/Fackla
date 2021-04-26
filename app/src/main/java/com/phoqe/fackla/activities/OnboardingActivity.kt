package com.phoqe.fackla.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.phoqe.fackla.BuildConfig
import com.phoqe.fackla.databinding.ActivityOnboardingBinding
import com.phoqe.fackla.fragments.OnboardingDeveloperModeFragment
import com.phoqe.fackla.fragments.OnboardingIntroFragment
import com.phoqe.fackla.fragments.OnboardingLocationPermissionFragment
import com.phoqe.fackla.fragments.OnboardingMockLocationFragment

private const val NUM_PAGES = 4

class OnboardingActivity : FragmentActivity() {
    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var viewPager: ViewPager2

    private inner class OnboardingPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        private val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this@OnboardingActivity)

        private var hasSelectedMockLocApp = false

        private fun isDev(): Boolean {
            return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
                Settings.Secure.getInt(contentResolver, Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0) == 1
            } else {
                Settings.Secure.getInt(contentResolver, Settings.Secure.DEVELOPMENT_SETTINGS_ENABLED, 0) == 1
            }
        }

        private fun hasAdequatePerms(): Boolean {
            return ContextCompat.checkSelfPermission(
                this@OnboardingActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        }

        private fun finishOnboarding() {
            with(sharedPrefs.edit()) {
                putBoolean("has_onboarded", true)

                apply()
            }

            startActivity(Intent(this@OnboardingActivity, MainActivity::class.java))
            finish()
        }

        override fun getItemCount(): Int = NUM_PAGES

        override fun createFragment(position: Int): Fragment {
            if (BuildConfig.DEBUG) {
                return when (position) {
                    0 -> OnboardingIntroFragment()
                    1 -> OnboardingDeveloperModeFragment()
                    2 -> OnboardingMockLocationFragment()
                    3 -> OnboardingLocationPermissionFragment()
                    else -> OnboardingIntroFragment()
                }
            }

            return when (position) {
                0 -> OnboardingIntroFragment()
                1 -> {
                    if (isDev()) {
                        hasSelectedMockLocApp = true

                        OnboardingMockLocationFragment()
                    } else {
                        OnboardingDeveloperModeFragment()
                    }
                }
                2 -> {
                    if (hasSelectedMockLocApp) {
                        if (hasAdequatePerms()) {
                            finishOnboarding()

                            OnboardingIntroFragment()
                        } else {
                            OnboardingLocationPermissionFragment()
                        }
                    } else {
                        OnboardingMockLocationFragment()
                    }
                }
                3 -> {
                    if (hasAdequatePerms()) {
                        finishOnboarding()

                        OnboardingIntroFragment()
                    } else {
                        OnboardingLocationPermissionFragment()
                    }
                }
                else -> OnboardingIntroFragment()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOnboardingBinding.inflate(layoutInflater)

        setContentView(binding.root)

        viewPager = binding.root

        viewPager.adapter = OnboardingPagerAdapter(this)
    }

    override fun onBackPressed() {
        if (viewPager.currentItem == 0) {
            super.onBackPressed()
        } else {
            viewPager.currentItem = viewPager.currentItem - 1
        }
    }
}