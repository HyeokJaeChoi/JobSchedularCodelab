package com.hyeok.jobschedulercodelab

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService

class NotificationJobService: JobService() {

    private val notificationManager by lazy { getSystemService<NotificationManager>() }
    private val PRIMARY_CHANNEL_ID by lazy { "primary_notification_channel" }

    override fun onStartJob(p0: JobParameters?): Boolean {
        createNotificationChannel()

        val contentPendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notificationBuilder = NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
            .setContentTitle("Job Service")
            .setContentText("Your Job ran to completion!")
            .setContentIntent(contentPendingIntent)
            .setSmallIcon(R.drawable.ic_job_running)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true)

        notificationManager?.notify(0, notificationBuilder.build())

        return false
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        return true
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                PRIMARY_CHANNEL_ID,
                "Job Service Notification",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
                description = "Notifications from Job Service"
            }

            notificationManager?.createNotificationChannel(notificationChannel)
        }
    }
}