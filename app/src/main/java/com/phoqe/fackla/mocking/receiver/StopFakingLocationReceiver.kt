package com.phoqe.fackla.mocking.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.phoqe.fackla.mocking.IntentAction
import com.phoqe.fackla.mocking.manager.FakeLocationManager
import timber.log.Timber

class StopFakingLocationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Timber.v("onReceive")

        Timber.d(intent.toString())

        if (!intent.action.equals(IntentAction.SAMSUNG_LOCATION_MOCK_DELETE) &&
            !intent.action.equals(IntentAction.STOP_FAKING_LOCATION)
        ) {
            Timber.e("Intent action not supported.")

            return
        }

        Timber.d("Intent action supported.")

        FakeLocationManager.getInstance(context).stop()
    }
}