package com.phoqe.fackla

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.localization.LocalizationPlugin
import com.phoqe.fackla.databinding.ActivityMainBinding
import timber.log.Timber
import java.lang.NullPointerException
import java.lang.RuntimeException

class MainActivity : AppCompatActivity(), PermissionsListener {
    private val TAG = javaClass.simpleName

    private var permsManager: PermissionsManager = PermissionsManager(this)

    private lateinit var binding: ActivityMainBinding
    private lateinit var map: MapboxMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupMapbox()

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        configMapView(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()

        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()

        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()

        binding.mapView.onPause()
    }

    override fun onStop() {
        super.onStop()

        binding.mapView.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        binding.mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()

        binding.mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()

        binding.mapView.onDestroy()
    }

    private fun setupMapbox() {
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))
    }

    private fun configMapView(savedInstanceState: Bundle?) {
        val mapView = binding.mapView

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { map ->
            this.map = map

            val ui = map.uiSettings

            ui.isCompassEnabled = false
            ui.isLogoEnabled = false
            ui.isAttributionEnabled = false

            map.setStyle(Style.MAPBOX_STREETS) {
                val lz = LocalizationPlugin(mapView, map, it)

                try {
                    lz.matchMapLanguageWithDeviceDefault()
                } catch (ex: NullPointerException) {
                    Timber.e(ex, "Failed to match map language with device default.")
                }

                enableLocationComponent(it)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun enableLocationComponent(fullyLoadedMapStyle: Style) {
        if (!PermissionsManager.areLocationPermissionsGranted(this)) {
            permsManager = PermissionsManager(this)

            permsManager.requestLocationPermissions(this)

            return
        }

        val options = LocationComponentOptions.builder(this)
                .build()

        val activationOptions = LocationComponentActivationOptions.builder(this, fullyLoadedMapStyle)
                .locationComponentOptions(options)
                .build()

        map.locationComponent.apply {
            activateLocationComponent(activationOptions)

            isLocationComponentEnabled = true
            cameraMode = CameraMode.TRACKING
            renderMode = RenderMode.NORMAL
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        permsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        Toast.makeText(this, "onExplanationNeeded", Toast.LENGTH_LONG).show()
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            enableLocationComponent(map.style!!)
        } else {
            Toast.makeText(this, "onPermissionResult", Toast.LENGTH_LONG).show();
        }
    }
}