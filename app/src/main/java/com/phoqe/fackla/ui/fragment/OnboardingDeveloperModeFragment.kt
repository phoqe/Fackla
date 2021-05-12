package com.phoqe.fackla.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.phoqe.fackla.databinding.FragmentOnboardingDeveloperModeBinding

class OnboardingDeveloperModeFragment() : Fragment() {
    interface OnBecomeDeveloperClickListener {
        fun onBecomeDeveloperClick()
    }

    var listener: OnBecomeDeveloperClickListener? = null

    private var _binding: FragmentOnboardingDeveloperModeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingDeveloperModeBinding.inflate(layoutInflater, container, false)

        binding.btnBecomeDeveloper.setOnClickListener {
            startActivity(Intent(android.provider.Settings.ACTION_DEVICE_INFO_SETTINGS))

            listener?.onBecomeDeveloperClick()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}