package com.phoqe.fackla.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.FragmentActivity
import androidx.preference.PreferenceManager
import androidx.viewpager2.widget.ViewPager2
import com.phoqe.fackla.BuildConfig
import com.phoqe.fackla.adapters.OnboardingPagerAdapter
import com.phoqe.fackla.databinding.ActivityOnboardingBinding
import com.phoqe.fackla.fragments.*

class OnboardingActivity : FragmentActivity(),
        OnboardingIntroFragment.OnGetStartedClickListener,
        OnboardingDeveloperModeFragment.OnBecomeDeveloperClickListener,
        OnboardingEndFragment.onStartClickListener,
        OnboardingLocationPermissionFragment.OnGrantedPermissionListener,
        OnboardingMockLocationFragment.OnSelectMockAppClickListener {
    private lateinit var prefs: SharedPreferences
    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var viewPager: ViewPager2

    private var skipSaveProgress = false

    private fun saveProgress() {
        with(prefs.edit()) {
            putInt("last_onboarding_item", viewPager.currentItem + 1)
            apply()
        }
    }

    private fun incrementPager(silently: Boolean = false) {
        if (silently) {
            skipSaveProgress = true
            saveProgress()
        } else {
            viewPager.currentItem += 1
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefs = PreferenceManager.getDefaultSharedPreferences(this)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)

        setContentView(binding.root)

        viewPager = binding.root
        viewPager.adapter = OnboardingPagerAdapter(this)

        // Developers can preview the Onboarding pages.
        viewPager.isUserInputEnabled = BuildConfig.DEBUG
    }

    override fun onResume() {
        super.onResume()

        viewPager.currentItem = prefs.getInt("last_onboarding_item", 0)
    }

    override fun onPause() {
        super.onPause()

        if (!skipSaveProgress) {
            saveProgress()
        }

        skipSaveProgress = false
    }

    override fun onGetStartedClick() {
        viewPager.currentItem += 1
    }

    override fun onStartClick() {
        with(prefs.edit()) {
            putBoolean("has_onboarded", true)
            apply()
        }

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onSelectMockAppClick() {
        incrementPager(true)
    }

    override fun onBecomeDeveloperClick() {
        incrementPager(true)
    }

    override fun onGrantedPermission() {
        incrementPager()
    }
}