package com.phoqe.fackla

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import androidx.annotation.RequiresApi
import timber.log.Timber

@RequiresApi(Build.VERSION_CODES.O)
class NotificationRegister(private val context: Context) {
    private val manager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

    init {
        Timber.v("init")

        createChannels()
    }

    private fun createChannels() {
        Timber.v("createChannels")

        createFakeLocationChannel()
    }

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