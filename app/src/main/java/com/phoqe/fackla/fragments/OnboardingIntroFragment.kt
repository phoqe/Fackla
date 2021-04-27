package com.phoqe.fackla.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.phoqe.fackla.databinding.FragmentOnboardingIntroBinding

class OnboardingIntroFragment(private val listener: OnGetStartedClickListener) : Fragment() {
    interface OnGetStartedClickListener {
        fun onGetStartedClick()
    }

    private var _binding: FragmentOnboardingIntroBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingIntroBinding.inflate(layoutInflater, container, false)

        binding.btnGetStarted.setOnClickListener {
            listener.onGetStartedClick()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}