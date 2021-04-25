package com.phoqe.fackla.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.phoqe.fackla.databinding.FragmentOnboardingMockLocationBinding

class OnboardingMockLocationFragment : Fragment() {
    private var _binding: FragmentOnboardingMockLocationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingMockLocationBinding.inflate(layoutInflater, container, false)

        binding.btnSelectMockLocApp.setOnClickListener {
            startActivity(Intent(android.provider.Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS))
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}