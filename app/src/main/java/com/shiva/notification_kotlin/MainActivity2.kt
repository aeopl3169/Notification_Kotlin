package com.shiva.notification_kotlin

import android.app.Notification
import android.app.Notification.EXTRA_NOTIFICATION_ID
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
//            action = ACTION_SNOOZE
            putExtra(EXTRA_NOTIFICATION_ID, 0)
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(this, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT)

        // For custom notification we use "RemoteViews"
        val contentVie = RemoteViews(packageName, R.layout.notification_layout)
        contentVie.setTextViewText(R.id.tv_title, "CodeAndroid")
        contentVie.setTextViewText(R.id.tv_content, "Text notification")

        createNotificationChannel()

        button.setOnClickListener {
                // Register the channel with the system - initializing
                notificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Notice that the NotificationCompat.Builder constructor requires that you provide a channel ID.
            //This is required for compatibility with Android 8.0 (API level 26) and higher, but is ignored by older versions.
                notificationCompatBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContent(contentVie)// to set the custom notification
                    .setSmallIcon(R.drawable.ic_launcher_round)
                    .setContentTitle("TITLE")
                    .setContentText("Content")
                    .setPriority(NotificationCompat.PRIORITY_MAX) // you must also set the priority with setPriority() to support Android 7.1 and lower
                    //  If you want your notification to be longer, you can enable an expandable notification by adding a style template with setStyle().
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE) //the system will make decisions about displaying your notification when the device is in Do Not Disturb mode.
                    .setStyle(
                        NotificationCompat.BigTextStyle()
                            .bigText("Much longer text that cannot fit one line...")
                    )
                    // Set the intent that will fire when the user taps the notification
                    .setContentIntent(pendingIntent) // To open the app (particular activity) when the user taps the notification.
                    .addAction(R.drawable.ic_launcher, getString(R.string.notification_btn),
                        pendingIntent) // To add an action button in the notification, pass a PendingIntent to the addAction() method.
                        // If your app targets Android 10 (API level 29) or higher, you must request the USE_FULL_SCREEN_INTENT permission in your app's manifest file
                    .setFullScreenIntent(pendingIntent, true) // used for alarms and calls.
                    .setAutoCancel(true) //which automatically removes the notification when the user taps it.
            with(NotificationManagerCompat.from(this)) {
                // notificationId is a unique int for each notification that you must define.
                // Remember to save the notification ID that you pass to NotificationManagerCompat.notify()
                // because you'll need it later if you want to update or remove the notification.
                notificationManager.notify(9999, notificationCompatBuilder.build())
            }

        }
    }
    private fun createNotificationChannel(){
        /* You must create the notification channel before posting any notifications on Android 8.0 (API 26) and higher,
        you should execute this code as soon as your app starts.
        It's safe to call this repeatedly because creating an existing notification channel performs no operation. */
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val notificationChannel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}