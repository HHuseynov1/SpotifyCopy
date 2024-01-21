package com.example.spotifycopy.domain.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.spotifycopy.R
import com.example.spotifycopy.domain.models.SongModel

private lateinit var songList : List<SongModel>

//class NotificationService(context: Context) {
//    val notificationManager = NotificationManagerCompat.from(context)
//
//    val notification = NotificationCompat.Builder(context, "mediaPlayerChannel")
//        .setSmallIcon(R.drawable.spotifylogo)
//        .setContentTitle("Media Player")
//        .setContentText("Playing your song")
//        .setPriority(NotificationCompat.PRIORITY_LOW)
//        .build()
//
//    init {
//        val channel = NotificationChannel(
//            "mediaPlayerChannel",
//            "Media Player Channel",
//            NotificationManager.IMPORTANCE_LOW
//        )
//        notificationManager.createNotificationChannel(channel)
//
//
//        if (ActivityCompat.checkSelfPermission(
//                context,
//                Manifest.permission.POST_NOTIFICATIONS
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//        }
//
//        notificationManager.notify(1, context, notification)
//    }

//}