package com.phoqe.fackla.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.phoqe.fackla.managers.FakeLocationManager
import timber.log.Timber

class StopFakingLocationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Timber.v("onReceive")

        if (!intent.action.equals("com.samsung.android.location.mock.delete")) {
            Timber.e("Intent action not supported.")

            return
        }

        Timber.d("Intent action supported.")
    }
}