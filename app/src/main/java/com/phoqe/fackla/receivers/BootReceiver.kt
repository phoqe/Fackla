package com.phoqe.fackla.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.preference.PreferenceManager
import com.mapbox.mapboxsdk.geometry.LatLng
import com.phoqe.fackla.managers.FakeLocationManager
import timber.log.Timber

class BootReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Timber.v("onReceive")
        Timber.d(intent.toString())

        if (context == null) {
            Timber.e("No context.")

            return
        }

        if (intent == null) {
            Timber.e("No intent.")

            return
        }

        if (!intent.action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            Timber.e("Intent Action not supported.")

            return
        }

        val prefs = PreferenceManager.getDefaultSharedPreferences(context)

        if (prefs.getBoolean("fake_loc_service_active", false)) {
            val lat = prefs.getLong("fake_lat", 0).toDouble()
            val long = prefs.getLong("fake_long", 0).toDouble()
            val alt = prefs.getLong("fake_alt", 0).toDouble()
            val point = LatLng(lat, long, alt)

            FakeLocationManager.getInstance(context).start(point)
        }
    }
}