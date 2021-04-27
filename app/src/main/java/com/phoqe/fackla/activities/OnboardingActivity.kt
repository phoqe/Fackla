package com.phoqe.fackla.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.preference.PreferenceManager
import androidx.viewpager2.widget.ViewPager2
import com.phoqe.fackla.adapters.OnboardingPagerAdapter
import com.phoqe.fackla.databinding.ActivityOnboardingBinding
import com.phoqe.fackla.fragments.*

class OnboardingActivity : FragmentActivity(),
        OnboardingIntroFragment.OnGetStartedClickListener,
        OnboardingEndFragment.onStartClickListener,
        OnboardingMockLocationFragment.OnSelectMockAppClickListener{
    private lateinit var prefs: SharedPreferences
    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var viewPager: ViewPager2

    private var skipSaveProgress = false

    private fun saveProgress(currentItem: Int = viewPager.currentItem) {
        with(prefs.edit()) {
            putInt("onboarding_item", currentItem)
            apply()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefs = PreferenceManager.getDefaultSharedPreferences(this)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)

        setContentView(binding.root)

        viewPager = binding.root
        viewPager.adapter = OnboardingPagerAdapter(this)
        viewPager.isUserInputEnabled = false
        viewPager.currentItem = prefs.getInt("onboarding_item", 0)
    }

    override fun onPause() {
        super.onPause()

        if (!skipSaveProgress) {
            saveProgress()
        }
    }

    override fun onBackPressed() {
        if (viewPager.currentItem == 0) {
            super.onBackPressed()
        } else {
            viewPager.currentItem = viewPager.currentItem - 1
        }
    }

    override fun onGetStartedClick() {
        viewPager.currentItem = 1
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
        skipSaveProgress = true
        saveProgress(2)
    }
}