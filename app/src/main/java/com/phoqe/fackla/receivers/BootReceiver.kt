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

        FakeLocationManager.getInstance(context).attemptStartFromPrefs()
    }
}