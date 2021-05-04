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

        if (context == null) {
            Timber.e("No context.")

            return
        }

        Timber.d("Context found.")

        if (intent == null) {
            Timber.e("No intent.")

            return
        }

        Timber.d("Intent found.")
        Timber.d(intent.toString())

        if (!intent.action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            Timber.e("Intent Action not supported.")

            return
        }

        Timber.d("Intent Action supported.")

        val prefs = PreferenceManager.getDefaultSharedPreferences(context)

        if (prefs.getBoolean("fake_loc_service_active", false)) {
            Timber.d("Fake Location Service was active prior to boot.")

            val default = java.lang.Double.doubleToRawLongBits(0.0)
            val lat = java.lang.Double.longBitsToDouble(prefs.getLong("fake_lat", default))
            val long = java.lang.Double.longBitsToDouble(prefs.getLong("fake_long", default))
            val alt = java.lang.Double.longBitsToDouble(prefs.getLong("fake_alt", default))
            val point = LatLng(lat, long, alt)

            Timber.d(point.toString())

            FakeLocationManager.getInstance(context).start(point)
        } else {
            Timber.d("Fake Location Service wasn't active prior to boot.")
        }
    }
}