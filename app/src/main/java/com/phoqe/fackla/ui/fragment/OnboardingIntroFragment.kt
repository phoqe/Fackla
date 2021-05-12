package com.phoqe.fackla.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.phoqe.fackla.databinding.FragmentOnboardingIntroBinding

class OnboardingIntroFragment() : Fragment() {
    interface OnGetStartedClickListener {
        fun onGetStartedClick()
    }

    var listener: OnGetStartedClickListener? = null

    private var _binding: FragmentOnboardingIntroBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingIntroBinding.inflate(layoutInflater, container, false)

        binding.btnGetStarted.setOnClickListener {
            listener?.onGetStartedClick()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}