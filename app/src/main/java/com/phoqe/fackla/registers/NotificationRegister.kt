package com.phoqe.fackla.registers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import androidx.annotation.RequiresApi
import com.phoqe.fackla.R
import timber.log.Timber

class NotificationRegister(private val context: Context) {
    private val manager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

    init {
        Timber.v("init")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Timber.i("Will register notification channels as this version of Android requires them.")

            createChannels()
        } else {
            Timber.i("Notification channels not registred as they are not supported on this version of Android.")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannels() {
        Timber.v("createChannels")

        createFakeLocationChannel()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createFakeLocationChannel() {
        Timber.v("createFakeLocationChannel")

        val channel = NotificationChannel(
            context.getString(R.string.fake_location_channel_id),
            context.getString(R.string.fake_location_channel_name),
            NotificationManager.IMPORTANCE_HIGH
        )

        channel.description = context.getString(R.string.fake_location_channel_description)
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE

        manager.createNotificationChannel(channel)
    }
}