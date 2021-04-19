package com.phoqe.fackla.services

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.phoqe.fackla.MainActivity
import com.phoqe.fackla.R

class FakeLocationNotificationService: Service() {
    private val notificationId = 1

    private fun createNotification(): Notification {
        val builder = NotificationCompat.Builder(this, getString(R.string.fake_location_channel_id))
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val action = NotificationCompat.Action(null, getString(R.string.fake_location_notification_action_title), pendingIntent)

        builder.setSmallIcon(R.mipmap.ic_launcher)
        builder.priority = NotificationCompat.PRIORITY_MAX
        builder.setWhen(System.currentTimeMillis())
        builder.setContentTitle(getString(R.string.fake_location_notification_title))
        builder.setContentText(getString(R.string.fake_location_notification_description))
        builder.setLocalOnly(true)
        builder.setCategory(NotificationCompat.CATEGORY_SERVICE)
        builder.setOngoing(true)
        builder.setContentIntent(pendingIntent)
        builder.addAction(action)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            builder.color = getColor(R.color.primary_color)
        }

        return builder.build()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        startForeground(notificationId, createNotification())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        return START_REDELIVER_INTENT
    }
}