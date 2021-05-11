package com.phoqe.fackla.services

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.phoqe.fackla.IntentAction
import com.phoqe.fackla.R
import com.phoqe.fackla.activities.MainActivity
import com.phoqe.fackla.receivers.StopFakingLocationReceiver

class FakeLocationNotificationService : Service() {
    private val notificationId = 1
    private val receiver = StopFakingLocationReceiver()

    private fun createStopAction(): NotificationCompat.Action {
        val intent = Intent(this, StopFakingLocationReceiver::class.java).apply {
            action = IntentAction.STOP_FAKING_LOCATION
        }
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            0
        )

        return NotificationCompat.Action(
            null,
            getString(R.string.fake_location_notification_action_title),
            pendingIntent
        )
    }

    private fun createNotification(): Notification {
        val builder = NotificationCompat.Builder(this, getString(R.string.fake_location_channel_id))
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        builder.setSmallIcon(R.drawable.ic_baseline_map_24)
        builder.priority = NotificationCompat.PRIORITY_MAX
        builder.setWhen(System.currentTimeMillis())
        builder.setContentTitle(getString(R.string.fake_location_notification_title))
        builder.setContentText(getString(R.string.fake_location_notification_description))
        builder.setLocalOnly(true)
        builder.setCategory(NotificationCompat.CATEGORY_SERVICE)
        builder.setOngoing(true)
        builder.setContentIntent(pendingIntent)
        builder.addAction(createStopAction())
        builder.setStyle(
            NotificationCompat.BigTextStyle()
                .bigText(getString(R.string.fake_location_notification_description))
        )

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

        val intents = arrayOf(
            IntentAction.SAMSUNG_LOCATION_MOCK_DELETE,
            IntentAction.STOP_FAKING_LOCATION
        )
        val intentFilter = IntentFilter()

        for (intent in intents) {
            intentFilter.addAction(intent)
        }

        registerReceiver(receiver, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()

        unregisterReceiver(receiver)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        
        return START_REDELIVER_INTENT
    }
}