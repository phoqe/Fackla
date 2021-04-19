package com.phoqe.fackla

import android.annotation.SuppressLint
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.localization.LocalizationPlugin
import com.mapbox.search.*
import com.mapbox.search.result.SearchResult
import com.phoqe.fackla.databinding.ActivityMainBinding
import com.phoqe.fackla.managers.FakeLocationManager
import timber.log.Timber
import java.lang.NullPointerException

class MainActivity : AppCompatActivity(), PermissionsListener, MapboxMap.OnMapLongClickListener {
    private var permsMgr: PermissionsManager = PermissionsManager(this)

    private lateinit var binding: ActivityMainBinding
    private lateinit var map: MapboxMap
    private lateinit var locMgr: LocationManager
    private lateinit var revGeoEngine: ReverseGeocodingSearchEngine
    private lateinit var fakeLocMgr: FakeLocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        locMgr = getSystemService(LOCATION_SERVICE) as LocationManager
        revGeoEngine = MapboxSearchSdk.createReverseGeocodingSearchEngine()
        fakeLocMgr = FakeLocationManager(this)

        setContentView(binding.root)

        configMapView(savedInstanceState)
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
            permsMgr = PermissionsManager(this)

            permsMgr.requestLocationPermissions(this)

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

    private fun showLocSnackbar(point: LatLng) {
        val opts = ReverseGeoOptions(
                center = Point.fromLngLat(point.latitude, point.longitude),
                limit = 1
        )

        revGeoEngine.search(opts, object : SearchCallback {
            override fun onError(e: Exception) {
                Snackbar.make(
                        binding.mapView,
                        "Latitude: ${point.latitude}\nLongitude: ${point.longitude}",
                        Snackbar.LENGTH_LONG
                ).show()
            }

            override fun onResults(results: List<SearchResult>, responseInfo: ResponseInfo) {
                if (results.isEmpty()) {
                    Snackbar.make(
                            binding.mapView,
                            "Latitude: ${point.latitude}\nLongitude: ${point.longitude}",
                            Snackbar.LENGTH_LONG
                    ).show()

                    return
                }

                Snackbar.make(
                        binding.mapView,
                        "Set fake location to ${results.first().name}.",
                        Snackbar.LENGTH_LONG
                ).show()
            }
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        permsMgr.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {

    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            enableLocationComponent(map.style!!)
        }
    }

    override fun onMapLongClick(point: LatLng): Boolean {
        fakeLocMgr.start(point) {
            showLocSnackbar(point)
        }

        return true
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
}