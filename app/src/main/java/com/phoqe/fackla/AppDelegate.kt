package com.phoqe.fackla

import android.app.Application
import com.mapbox.mapboxsdk.Mapbox
import com.phoqe.fackla.registers.NotificationRegister
import timber.log.Timber

class AppDelegate: Application() {
    private lateinit var notificationRegister: NotificationRegister

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))

        notificationRegister = NotificationRegister(this)
    }
}