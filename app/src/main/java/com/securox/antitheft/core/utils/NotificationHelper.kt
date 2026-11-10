package com.securox.antitheft.core.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.securox.antitheft.R
import com.securox.antitheft.core.constants.AppConstants
import com.securox.antitheft.presentation.MainActivity

object NotificationHelper {

    fun createNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Security Alerts Channel
            val securityChannel = NotificationChannel(
                AppConstants.CHANNEL_SECURITY_ALERTS,
                context.getString(R.string.notification_channel_security),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = context.getString(R.string.notification_channel_security_desc)
                enableVibration(true)
                enableLights(true)
            }

            // Location Foreground Service Channel
            val locationChannel = NotificationChannel(
                AppConstants.CHANNEL_LOCATION_SERVICE,
                context.getString(R.string.notification_channel_location),
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = context.getString(R.string.notification_channel_location_desc)
                setShowBadge(false)
            }

            notificationManager.createNotificationChannel(securityChannel)
            notificationManager.createNotificationChannel(locationChannel)
        }
    }

    fun buildLocationServiceNotification(context: Context): Notification {
        val launchIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            launchIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(context, AppConstants.CHANNEL_LOCATION_SERVICE)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(context.getString(R.string.location_service_notification_title))
            .setContentText(context.getString(R.string.location_service_notification_text))
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
    }

    fun showSecurityAlert(context: Context, id: Int, title: String, message: String) {
        val launchIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            id,
            launchIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context, AppConstants.CHANNEL_SECURITY_ALERTS)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .build()

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(id, notification)
    }
}
