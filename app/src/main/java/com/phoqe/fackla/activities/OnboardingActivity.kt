package com.phoqe.fackla.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.phoqe.fackla.databinding.ActivityOnboardingBinding
import com.phoqe.fackla.fragments.OnboardingIntroFragment
import com.phoqe.fackla.fragments.OnboardingLocationPermissionFragment

private const val NUM_PAGES = 2

class OnboardingActivity : FragmentActivity() {
    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var viewPager: ViewPager2

    private inner class OnboardingPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = NUM_PAGES

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> OnboardingIntroFragment()
                1 -> OnboardingLocationPermissionFragment()
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