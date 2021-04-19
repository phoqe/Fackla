package com.phoqe.fackla

import android.app.Application
import com.mapbox.mapboxsdk.Mapbox
import timber.log.Timber

class App: Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))
    }
}