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
import android.os.Bundle
import android.view.View
import android.widget.RemoteViews
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity2 : AppCompatActivity() {


    private val CHANNEL_ID = "com.shiva.notification_kotlin"
    lateinit var notificationCompatBuilder: NotificationCompat.Builder
    lateinit var notificationManager: NotificationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Create an explicit intent for an Activity in your app
        val intent2 = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(this, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT)

        // For custom notification we use "RemoteViews"
        val contentVie = RemoteViews(packageName, R.layout.notification_layout)
        contentVie.setTextViewText(R.id.tv_title, "CodeAndroid")
        contentVie.setTextViewText(R.id.tv_content, "Text notification")


        button.setOnClickListener {
                // Register the channel with the system - initializing
                notificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationCompatBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContent(contentVie)// to set the custom notification
                    .setSmallIcon(R.drawable.ic_launcher_round)
                    .setContentTitle("TITLE")
                    .setContentText("Content")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    //  If you want your notification to be longer, you can enable an expandable notification by adding a style template with setStyle().
                    .setStyle(
                        NotificationCompat.BigTextStyle()
                            .bigText("Much longer text that cannot fit one line...")
                    )
                    // Set the intent that will fire when the user taps the notification
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
            with(NotificationManagerCompat.from(this)) {
                // notificationId is a unique int for each notification that you must define
                notificationManager.notify(9999, notificationCompatBuilder.build())
            }
        }

    }
}