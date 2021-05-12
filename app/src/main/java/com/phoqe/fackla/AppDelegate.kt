package com.phoqe.fackla

import androidx.multidex.MultiDexApplication
import com.mapbox.mapboxsdk.Mapbox
import timber.log.Timber

class AppDelegate : MultiDexApplication() {
    private lateinit var firebaseSession: FirebaseSession
    private lateinit var notificationRegister: NotificationRegister

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        firebaseSession = FirebaseSession()
        notificationRegister = NotificationRegister(this)

        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))
    }
}