package com.phoqe.fackla

import android.app.Application
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.search.MapboxSearchSdk
import com.mapbox.search.location.DefaultLocationProvider
import timber.log.Timber

class FacklaApp: Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))
        MapboxSearchSdk.initialize(this, getString(R.string.mapbox_access_token), DefaultLocationProvider(this))
    }
}