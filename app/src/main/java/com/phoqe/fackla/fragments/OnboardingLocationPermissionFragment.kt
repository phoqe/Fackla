package com.phoqe.fackla.fragments

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.phoqe.fackla.R
import com.phoqe.fackla.activities.OnboardingActivity
import com.phoqe.fackla.databinding.FragmentOnboardingLocationPermissionBinding

class OnboardingLocationPermissionFragment(private val activity: OnboardingActivity) : Fragment() {
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
                activity.onGrantedPermission()
            }
            shouldShowRequestPermissionRationale(perm) -> {
                showExplanationDialog()
            }
            else -> {
                reqPermLauncher.launch(perm)
            }
        }
    }

    private fun showExplanationDialog() {
        context?.let {
            MaterialAlertDialogBuilder(it)
                    .setTitle(R.string.onboarding_location_permission_dialog_title)
                    .setMessage(R.string.onboarding_location_permission_dialog_message)
                    .setNegativeButton("Cancel") { dialog, which ->
                        dialog.cancel()
                    }
                    .setPositiveButton("Manage") { dialog, which ->
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val data = Uri.fromParts("package", requireContext().packageName, null)

                        intent.setData(data)

                        startActivity(intent)
                    }
                    .show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        reqPermLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                activity.onGrantedPermission()
            } else {
                showExplanationDialog()
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