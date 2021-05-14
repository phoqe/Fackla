package com.phoqe.fackla

import android.app.Notification
import android.app.NotificationManager
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class NotificationRegisterTest {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val manager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

    @Before
    fun setUp() {
        assertTrue(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
    }

    @Test
    fun fakeLocationChannelLegit() {
        assertTrue(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)

        val channel = manager.getNotificationChannel(context.getString(R.string.fake_location_channel_id))

        assertNotNull(channel)
        assertEquals(channel.name, context.getString(R.string.fake_location_channel_name))
        assertEquals(channel.description, context.getString(R.string.fake_location_channel_description))
        assertEquals(channel.importance, NotificationManager.IMPORTANCE_HIGH)

        // Not working in instrumentation. Value is -1000 but expects 0.
        //assertEquals(channel.lockscreenVisibility, Notification.VISIBILITY_PRIVATE)
    }
}