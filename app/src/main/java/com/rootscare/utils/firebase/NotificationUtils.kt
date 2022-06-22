package com.rootscare.utils.firebase

import android.app.*
import android.app.ActivityManager.RunningAppProcessInfo
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.text.Html
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.core.app.NotificationCompat
import com.rootscare.serviceprovider.R
import com.rootscare.twilio.SoundPoolManager
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class NotificationUtils(context: Context) {
    private val TAG = NotificationUtils::class.java.simpleName

    private var mContext: Context? = context

    fun showNotificationMessage(title: String?, message: String, intent: Intent) {
        showNotificationMessage(title, message, intent, null)
    }

    private fun showNotificationMessage(
        title: String?, message: String, intent: Intent, imageUrl: String?
    ) {
        println("message $message")
        // Check for empty push message
        if (TextUtils.isEmpty(message)) return

        // notification icon
        val icon = R.drawable.ico_notification
        val whiteIcon: Int = R.drawable.ico_notification
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val resultPendingIntent =
            PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        val mBuilder = NotificationCompat.Builder(
            mContext!!, Config.AL_PUSH_NOTIFICATION
        )
        val alarmSound =
            Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + mContext!!.packageName + "/raw/notification")
        if (!TextUtils.isEmpty(imageUrl)) {
            if (imageUrl!!.length > 4 && Patterns.WEB_URL.matcher(imageUrl).matches()) {
                val bitmap = getBitmapFromURL(imageUrl)
                if (bitmap != null) {
                    showBigNotification(
                        bitmap, mBuilder, icon, title, message, resultPendingIntent, alarmSound
                    )
                } else {
                    showSmallNotification(
                        mBuilder, whiteIcon, icon, title, message, resultPendingIntent
                    )
                }
            }
        } else {
            showSmallNotification(mBuilder, whiteIcon, icon, title, message, resultPendingIntent)
            //            playNotificationSound();
        }
    }


    private fun showSmallNotification(
        mBuilder: NotificationCompat.Builder, white_icon: Int, icon: Int, title: String?,
        message: String, resultPendingIntent: PendingIntent
    ) {
        val inboxStyle = NotificationCompat.InboxStyle()
        inboxStyle.addLine(message)
        val notification =
            mBuilder.setSmallIcon(getNotificationIcon(white_icon, icon)).setTicker(title)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setVibrate(longArrayOf(100, 250))
                .setStyle(inboxStyle)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(getNotificationIcon(white_icon, icon))
                .setLargeIcon(BitmapFactory.decodeResource(mContext!!.resources, icon))
                .setContentText(message)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                .build()
        val notificationManager =
            mContext!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = Config.AL_PUSH_NOTIFICATION
        val channelName: CharSequence = "Some Channel"
        var importance = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            importance = NotificationManager.IMPORTANCE_HIGH
        }
        val notificationChannel: NotificationChannel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(channelId, channelName, importance)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.setShowBadge(true)
            notificationChannel.vibrationPattern =
                longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        //        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Random().nextInt(), notification)
    }

    private fun getNotificationIcon(whiteIcon: Int, icon: Int): Int {
        val isWhiteIcon = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1
        Log.e(TAG, isWhiteIcon.toString())
        return if (isWhiteIcon) whiteIcon else icon
    }

    private fun showBigNotification(
        bitmap: Bitmap, mBuilder: NotificationCompat.Builder, icon: Int,
        title: String?, message: String, resultPendingIntent: PendingIntent, alarmSound: Uri
    ) {
        val bigPictureStyle = NotificationCompat.BigPictureStyle()
        bigPictureStyle.setBigContentTitle(title)
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString())
        bigPictureStyle.bigPicture(bitmap)
        val notification = mBuilder.setSmallIcon(icon).setTicker(title)
            .setAutoCancel(true)
            .setContentTitle(title)
            .setContentIntent(resultPendingIntent)
            .setSound(alarmSound)
            .setStyle(bigPictureStyle)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.drawable.ico_notification)
            .setLargeIcon(BitmapFactory.decodeResource(mContext!!.resources, icon))
            .setContentText(message)
            .build()
        val notificationManager =
            mContext!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = Config.AL_PUSH_NOTIFICATION
        val channelName: CharSequence = "Some Channel"
        var importance = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            importance = NotificationManager.IMPORTANCE_HIGH
        }
        val notificationChannel: NotificationChannel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(channelId, channelName, importance)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.vibrationPattern =
                longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            notificationManager.createNotificationChannel(notificationChannel)
        }
//        NotificationManager notificationManager =(NotificationManager) mContext . getSystemService (Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Config.NOTIFICATION_ID_BIG_IMAGE, notification)
    }

    /**
     * Downloading push notification image before displaying it in the notification tray
     */
    private fun getBitmapFromURL(strURL: String?): Bitmap? {
        return try {
            val url = URL(strURL)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    // Playing notification sound
    fun playNotificationSound() {
        try {
            val alarmSound =
                Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + mContext!!.packageName + "/raw/notification")
            val r = RingtoneManager.getRingtone(mContext, alarmSound)
            r.play()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Method checks if the app is in background or not
     */
    fun isAppIsInBackground(context: Context): Boolean {
        var isInBackground = true
        val am =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningProcesses =
            am.runningAppProcesses
        for (processInfo in runningProcesses) {
            if (processInfo.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                for (activeProcess in processInfo.pkgList) {
                    if (activeProcess == context.packageName) {
                        isInBackground = false
                    }
                }
            }
        }
        return isInBackground
    }

    // Clears notification tray messages
    fun clearNotifications(context: Context) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }

    private fun getTimeMilliSec(timeStamp: String?): Long {
        val format =
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        try {
            val date = format.parse(timeStamp)
            return date.time
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return 0
    }


    fun showCallNotification(title: String?, message: String?, intent: Intent) {
        val icon = R.drawable.ico_notification
        val whiteIcon: Int = R.drawable.ico_notification
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val resultPendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        mContext?.let { SoundPoolManager.getInstance(it)?.playRinging() }
        val mBuilder = NotificationCompat.Builder(mContext!!, Config.AL_PUSH_NOTIFICATION)
        if (title != null) {
            if (message != null) {
                showCallNotificationMessage(mBuilder, whiteIcon, icon, title, message, resultPendingIntent)
            }
        }
    }

    private fun showCallNotificationMessage(
        mBuilder: NotificationCompat.Builder, white_icon: Int, icon: Int, title: String, message: String,
        resultPendingIntent: PendingIntent
    ) {
        val notification = mBuilder.setSmallIcon(getNotificationIcon(white_icon, icon)).setTicker(title)
            .setAutoCancel(true)
            .setContentTitle(title)
            .setContentIntent(resultPendingIntent)
            .setVibrate(longArrayOf(100, 250))
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setWhen(getLocalDateTime())
            .setSmallIcon(getNotificationIcon(white_icon, icon))
            .setLargeIcon(BitmapFactory.decodeResource(mContext!!.resources, icon))
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setSound(Uri.parse("android.resource://" + mContext!!.packageName + "/" + R.raw.incoming))
            .setFullScreenIntent(resultPendingIntent, true)
            .build()
        println("resultPendingIntent $resultPendingIntent")
        mBuilder.notification.flags = mBuilder.notification.flags or Notification.FLAG_AUTO_CANCEL
        //        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        val notificationManager = mContext!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = Config.AL_PUSH_NOTIFICATION
        val channelName: CharSequence = "Some Channel"
        var importance = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            importance = NotificationManager.IMPORTANCE_HIGH
        }
        val notificationChannel: NotificationChannel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(channelId, channelName, importance)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 100)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(Random().nextInt(), notification)
    }

    private fun getLocalDateTime(): Long {
        val cal = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault())
        val currentLocalTime = cal.time
        val date: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        date.timeZone = TimeZone.getDefault()
        return currentLocalTime.time
    }

}