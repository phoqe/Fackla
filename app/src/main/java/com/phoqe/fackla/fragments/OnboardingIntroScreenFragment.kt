package com.phoqe.fackla.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.phoqe.fackla.databinding.FragmentOnboardingIntroScreenBinding
import com.phoqe.fackla.databinding.FragmentOnboardingScreenBinding

class OnboardingIntroScreenFragment : Fragment() {
    private var _binding: FragmentOnboardingIntroScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingIntroScreenBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}