package com.exam.sample.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.exam.sample.App
import com.exam.sample.R
import com.exam.sample.ui.MainActivity
import com.exam.sample.utils.Const

object DataChangeNotification {

    lateinit var notificationBuilder: NotificationCompat.Builder
    lateinit var notificationManager: NotificationManager

    fun createNotification(context: Context, content: String): NotificationCompat.Builder {
        val notificationIntent = Intent(context, MainActivity::class.java)
        notificationIntent.action = ServiceActions.START_APP
        notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        notificationBuilder = NotificationCompat.Builder(context, Const.NOTIFICATION_CHANNEL_ID)

        notificationBuilder
            .setContentTitle(App.getApplication().getString(R.string.notiMessage))
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_launcher)
            //    .setOngoing(true)
            .setVibrate(longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400))
            .setContentIntent(pendingIntent)
//        .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                Const.NOTIFICATION_CHANNEL_ID, Const.NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            serviceChannel.enableVibration(true)
            serviceChannel.vibrationPattern = longArrayOf(
                100,
                200,
                300,
                400,
                500,
                400,
                300,
                200,
                400
            )
            serviceChannel.description = App.getApplication().getString(R.string.notiMessage)
            notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(serviceChannel)
        }
        return notificationBuilder
    }

    fun updateMessage(message: String) {
        notificationBuilder.setContentText(message)
        notificationManager.notify(Const.NOTI_ID, notificationBuilder.build())
    }
}
