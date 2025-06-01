package com.example.mcanalytics

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.mcanalytics.domain.AnalyticsEvent

fun Context.sendEventNotification(event: AnalyticsEvent) {
    val channelId = "EVENT_NOTIFICATION_CHANNEL"
    createNotificationChannel(channelId)

    val notification = NotificationCompat.Builder(this, channelId)
        .setSmallIcon(R.drawable.baseline_notifications_24) // Use a relevant icon
        .setContentTitle("Event Sent: ${event.eventName}")
        .setContentText("Integration: ${event.integration}, Data: ${event.data}")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .build()
    if (ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
    ) return
    with(NotificationManagerCompat.from(this)) {
        notify(event.timestamp.toInt(), notification)
    }
}

private fun Context.createNotificationChannel(channelId: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "Event Notifications"
        val descriptionText = "Notifications for all events sent"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
