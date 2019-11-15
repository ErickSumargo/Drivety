package com.example.drivety.utils

import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.drivety.R
import com.example.drivety.data.Payload
import com.example.drivety.DrivetyApplication.Companion.getContext as context

/**
 * Created by ericksumargo on 01/11/19
 */

object NotificationManager {

    private const val NOTIFICATION_ID: Int = 1

    fun build(data: Payload) {
        val builder = NotificationCompat.Builder(context())
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(data.title)
            .setContentText(data.description)

        val manager = NotificationManagerCompat.from(context()!!)
        manager.notify(NOTIFICATION_ID, builder.build())
    }
}
