package com.phoqe.fackla

import android.os.Build
import androidx.multidex.MultiDexApplication
import com.mapbox.mapboxsdk.Mapbox
import timber.log.Timber

class AppDelegate : MultiDexApplication() {
    private lateinit var firebaseSession: FirebaseSession

    private var notificationRegister: NotificationRegister? = null

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        firebaseSession = FirebaseSession()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationRegister = NotificationRegister(this)
        }

        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))
    }
}