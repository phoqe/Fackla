package com.phoqe.fackla.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.phoqe.fackla.databinding.FragmentOnboardingEndBinding

class OnboardingEndFragment() : Fragment() {
    interface onStartClickListener {
        fun onStartClick()
    }

    var listener: onStartClickListener? = null

    private var _binding: FragmentOnboardingEndBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingEndBinding.inflate(layoutInflater, container, false)

        binding.btnStart.setOnClickListener {
            listener?.onStartClick()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}