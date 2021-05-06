package com.phoqe.fackla.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
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
import com.phoqe.fackla.R
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
    private lateinit var noPermsDialog: AlertDialog

    private fun hasPerms(): Boolean {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
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
            if (hasPerms()) {
                activateLocationComponent(activationOptions)

                isLocationComponentEnabled = true
            }

            cameraMode = CameraMode.TRACKING
            renderMode = RenderMode.NORMAL
        }
    }

    private fun updateLocationPostStateChange(loc: Location) {
        val locUpd = LocationUpdate.Builder()
                .location(loc)
                .build()

        binding.mapView.getMapAsync { map ->
            map.locationComponent.forceLocationUpdate(locUpd)
        }
    }

    private fun showNoMockLocAppDialog() {
        MaterialAlertDialogBuilder(this)
                .setTitle(R.string.main_no_mock_loc_app_dialog_title)
                .setMessage(R.string.main_no_mock_loc_app_message)
                .setCancelable(false)
                .setNegativeButton(R.string.main_no_loc_app_negative_button) { dialog, _ ->
                    dialog.cancel()
                }
                .setPositiveButton(R.string.main_no_loc_app_positive_button) { _, _ ->
                    startActivity(Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS))
                }
                .show()
    }

    private fun startFakingLocation(point: LatLng) {
        if (!isFakingLocation && map.locationComponent.isLocationComponentActivated) {
            lastLocBeforeFaking = map.locationComponent.lastKnownLocation
        }

        try {
            FakeLocationManager.getInstance(this).start(point)
        } catch (ex: SecurityException) {
            showNoMockLocAppDialog()
        }
    }

    private fun stopFakingLocation() {
        FakeLocationManager.getInstance(this).stop()
        lastLocBeforeFaking?.let { loc -> updateLocationPostStateChange(loc) }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onFakeLocationManagerStart(event: FakeLocationManagerStartEvent) {
        EventBus.getDefault().removeStickyEvent(event)

        isFakingLocation = true

        updateLocationPostStateChange(event.fakeLocation)

        binding.efabStopFakingLocation.visibility = View.VISIBLE
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onFakeLocationManagerStop(event: FakeLocationManagerStopEvent) {
        EventBus.getDefault().removeStickyEvent(event)

        isFakingLocation = false

        lastLocBeforeFaking?.let { loc -> updateLocationPostStateChange(loc) }

        binding.efabStopFakingLocation.visibility = View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        locMgr = getSystemService(LOCATION_SERVICE) as LocationManager
        noPermsDialog = MaterialAlertDialogBuilder(this)
                .setTitle(R.string.main_no_perms_dialog_title)
                .setMessage(R.string.main_no_perms_dialog_message)
                .setCancelable(false)
                .setPositiveButton(R.string.main_no_perms_dialog_positive_button) { dialog, _ ->
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val data = Uri.fromParts("package", packageName, null)

                    intent.setData(data)

                    startActivity(intent)
                }
                .create()

        setContentView(binding.root)

        binding.efabStopFakingLocation.setOnClickListener {
            stopFakingLocation()
        }

        configMapView(savedInstanceState)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

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

        if (!hasPerms()) {
            noPermsDialog.show()
        } else {
            noPermsDialog.cancel()

            binding.mapView.getMapAsync { map ->
                map.style?.let { enableLocationComponent(it) }
            }
        }
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