package com.phoqe.fackla.activities

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.LocationUpdate
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.localization.LocalizationPlugin
import com.phoqe.fackla.databinding.ActivityMainBinding
import com.phoqe.fackla.events.FakeLocationManagerStartEvent
import com.phoqe.fackla.events.FakeLocationManagerStopEvent
import com.phoqe.fackla.managers.FakeLocationManager
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber
import java.lang.NullPointerException

class MainActivity : AppCompatActivity(), PermissionsListener, MapboxMap.OnMapLongClickListener {
    private var permsMgr: PermissionsManager = PermissionsManager(this)
    private var lastLocBeforeFaking: Location? = null
    private var isFakingLocation = false

    private lateinit var binding: ActivityMainBinding
    private lateinit var map: MapboxMap
    private lateinit var locMgr: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        locMgr = getSystemService(LOCATION_SERVICE) as LocationManager

        setContentView(binding.root)

        binding.efabStopFakingLocation.setOnClickListener {
            stopFakingLocation()
        }

        configMapView(savedInstanceState)
    }

    private fun getMapStyle(): String {
        return when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> Style.DARK
            else -> Style.MAPBOX_STREETS
        }
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
            map.setStyle(getMapStyle()) {
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

    private fun updateLocationPostStateChange(loc: Location) {
        val locUpd = LocationUpdate.Builder()
                .location(loc)
                .build()

        map.locationComponent.forceLocationUpdate(locUpd)
    }

    private fun startFakingLocation(point: LatLng) {
        if (!isFakingLocation) {
            lastLocBeforeFaking = map.locationComponent.lastKnownLocation
        }

        FakeLocationManager.getInstance(this).start(point)
    }

    private fun stopFakingLocation() {
        FakeLocationManager.getInstance(this).stop()
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    fun onFakeLocationManagerStartEvent(event: FakeLocationManagerStartEvent) {
        isFakingLocation = true

        updateLocationPostStateChange(event.fakeLocation)

        binding.efabStopFakingLocation.visibility = View.VISIBLE
    }

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    fun onFakeLocationManagerStopEvent(event: FakeLocationManagerStopEvent) {
        isFakingLocation = false

        lastLocBeforeFaking?.let { loc -> updateLocationPostStateChange(loc) }

        binding.efabStopFakingLocation.visibility = View.GONE
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
        startFakingLocation(point)

        return true
    }

    override fun onStart() {
        super.onStart()

        binding.mapView.onStart()
        EventBus.getDefault().register(this)
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
        EventBus.getDefault().unregister(this)
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