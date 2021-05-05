package com.phoqe.fackla.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.phoqe.fackla.IntentAction
import com.phoqe.fackla.managers.FakeLocationManager
import timber.log.Timber

class StopFakingLocationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Timber.v("onReceive")

        Timber.d(intent.toString())

        if (!intent.action.equals("com.samsung.android.location.mock.delete") &&
                !intent.action.equals(IntentAction.STOP_FAKING_LOCATION)) {
            Timber.e("Intent action not supported.")

            return
        }

        Timber.d("Intent action supported.")

        FakeLocationManager.getInstance(context).stop()
    }
}