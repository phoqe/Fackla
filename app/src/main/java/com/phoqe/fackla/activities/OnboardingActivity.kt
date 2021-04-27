package com.phoqe.fackla.activities

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.Settings
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.preference.PreferenceManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.phoqe.fackla.BuildConfig
import com.phoqe.fackla.adapters.OnboardingPagerAdapter
import com.phoqe.fackla.databinding.ActivityOnboardingBinding
import com.phoqe.fackla.fragments.*

private const val NUM_PAGES = 5

class OnboardingActivity : FragmentActivity(), OnboardingEndFragment.OnGetStartedClickListener {
    private lateinit var prefs: SharedPreferences
    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefs = PreferenceManager.getDefaultSharedPreferences(this)
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

    override fun onGetStartedClick() {
        with(prefs.edit()) {
            putBoolean("has_onboarded", true)
            apply()
        }

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}