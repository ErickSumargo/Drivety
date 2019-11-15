package com.example.drivety.firebase

import android.util.Log
import com.example.drivety.data.Payload
import com.example.drivety.utils.NotificationManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.GsonBuilder

/**
 * Created by ericksumargo on 01/11/19
 */

class FirebaseNotification : FirebaseMessagingService() {

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        val data = p0.data.toString()
        val payload = GsonBuilder().create().fromJson(data, Payload::class.java)

        // Handle payload data.
        Log.d(TAG, "Message Received: $data")
        NotificationManager.build(payload)
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Log.d(TAG, "New Token: $p0")
    }

    companion object {
        private val TAG: String = FirebaseNotification::class.java.simpleName
    }
}
