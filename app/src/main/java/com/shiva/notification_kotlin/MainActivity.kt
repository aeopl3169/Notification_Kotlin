package com.shiva.notification_kotlin

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var notificationBuild: Notification.Builder
    private val CHANNEL_ID = "com.shiva.notification_kotlin"
    private val description = "Test notification"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

          btn_notify.setOnClickListener {

              val intent = Intent(this, MainActivity::class.java)
              val pendingIntent =
                  PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

              // For custom notification we use "RemoteViews"
              val contentVie = RemoteViews(packageName, R.layout.notification_layout)
              contentVie.setTextViewText(R.id.tv_title, "CodeAndroid")
              contentVie.setTextViewText(R.id.tv_content, "Text notification")

              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                  // API 26 or above 26
                  Log.d("version", "" + Build.VERSION.SDK_INT)
                  notificationChannel =
                      NotificationChannel(
                          CHANNEL_ID,
                          description,
                          NotificationManager.IMPORTANCE_HIGH
                      )
                  notificationChannel.enableLights(true)
                  notificationChannel.lightColor = Color.GREEN
                  notificationChannel.enableVibration(true)
                  notificationManager.createNotificationChannel(notificationChannel)

                  notificationBuild = Notification.Builder(this, CHANNEL_ID)
                      .setContent(contentVie)// to set the custom notification
                      .setSmallIcon(R.drawable.ic_launcher_round)
                      .setLargeIcon(
                          BitmapFactory.decodeResource(
                              this.resources,
                              R.drawable.ic_launcher
                          )
                      )
                      .setContentIntent(pendingIntent)
              } else {
                  // API less than 26

                  notificationBuild = Notification.Builder(this)
                      .setContent(contentVie)
                      .setSmallIcon(R.drawable.ic_launcher_round)
                      .setLargeIcon(
                          BitmapFactory.decodeResource(
                              this.resources,
                              R.drawable.ic_launcher
                          )
                      )
                      .setContentIntent(pendingIntent)
              }
              // Use unique id for each notification. Using the id we can cancel the notification too.
              notificationManager.notify(1234, notificationBuild.build())
          }
    }

}
