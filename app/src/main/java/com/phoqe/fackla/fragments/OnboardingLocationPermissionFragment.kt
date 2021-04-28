package com.phoqe.fackla.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.phoqe.fackla.databinding.FragmentOnboardingLocationPermissionBinding

class OnboardingLocationPermissionFragment(private val listener: OnGrantedPermissionListener) : Fragment() {
    interface OnGrantedPermissionListener {
        fun onGrantedPermission()
    }

    private var _binding: FragmentOnboardingLocationPermissionBinding? = null
    private val binding get() = _binding!!
    private lateinit var reqPermLauncher: ActivityResultLauncher<String>

    private fun requestPermission() {
        val perm = Manifest.permission.ACCESS_FINE_LOCATION

        when {
            ContextCompat.checkSelfPermission(
                    requireContext(),
                    perm
            ) == PackageManager.PERMISSION_GRANTED -> {
                listener.onGrantedPermission()
            }
            shouldShowRequestPermissionRationale(perm) -> {
                // Show info
            }
            else -> {
                reqPermLauncher.launch(perm)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        reqPermLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                listener.onGrantedPermission()
            } else {
                // Explain
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingLocationPermissionBinding.inflate(layoutInflater, container, false)

        binding.btnReviewPermission.setOnClickListener {
            requestPermission()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}