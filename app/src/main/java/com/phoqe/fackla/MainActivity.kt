package com.phoqe.fackla

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.widget.Toast
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.geometry.LatLng
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

class MainActivity : AppCompatActivity(), PermissionsListener, MapboxMap.OnMapLongClickListener {
    private val TEST_PROVIDER = LocationManager.GPS_PROVIDER

    private var permsManager: PermissionsManager = PermissionsManager(this)

    private lateinit var binding: ActivityMainBinding
    private lateinit var map: MapboxMap
    private lateinit var locMgr: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupMapbox()

        binding = ActivityMainBinding.inflate(layoutInflater)
        locMgr = getSystemService(LOCATION_SERVICE) as LocationManager

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

            map.addOnMapLongClickListener(this)

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
        // Ignored
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            enableLocationComponent(map.style!!)
        }
    }

    override fun onMapLongClick(point: LatLng): Boolean {
        Toast.makeText(this, point.toString(), Toast.LENGTH_LONG).show()

        setMockLocation(point)

        return true
    }

    private fun setMockLocation(cde: LatLng) {
        locMgr.addTestProvider(
                TEST_PROVIDER,
                false,
                false,
                false,
                false,
                true,
                false,
                false,
                1, // POWER_USAGE_LOW
                1 // ACCURACY_FINE
        )

        val loc = Location(TEST_PROVIDER)

        loc.latitude = cde.latitude
        loc.longitude = cde.longitude
        loc.altitude = cde.altitude
        loc.time = System.currentTimeMillis()
        loc.accuracy = 1F

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            loc.elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos()
        }

        locMgr.setTestProviderLocation(TEST_PROVIDER, loc)
        locMgr.setTestProviderEnabled(TEST_PROVIDER, true)
    }

    private fun removeMockLocation() {
        locMgr.removeTestProvider(TEST_PROVIDER)
    }
}