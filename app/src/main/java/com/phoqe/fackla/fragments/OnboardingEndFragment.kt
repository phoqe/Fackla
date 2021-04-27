package com.phoqe.fackla.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.phoqe.fackla.databinding.FragmentOnboardingEndBinding

class OnboardingEndFragment(listener: OnGetStartedClickListener) : Fragment() {
    interface OnGetStartedClickListener {
        fun onGetStartedClick()
    }

    private var _binding: FragmentOnboardingEndBinding? = null
    private val binding get() = _binding!!
    private val listener: OnGetStartedClickListener = listener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingEndBinding.inflate(layoutInflater, container, false)

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