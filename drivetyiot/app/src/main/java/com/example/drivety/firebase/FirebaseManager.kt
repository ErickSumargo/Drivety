package com.example.drivety.firebase

import com.example.drivety.data.Payload
import com.example.drivety.utils.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by ericksumargo on 01/11/19
 */

object FirebaseManager {

    private val httpConnection by lazy {
        URL("http://fcm.googleapis.com/fcm/send").openConnection() as HttpURLConnection
    }

    fun sendNotification(data: Payload) {
        GlobalScope.launch(Dispatchers.IO) {
            httpConnection.requestMethod = "POST"
            httpConnection.doInput = true
            httpConnection.doOutput = true

            httpConnection.setRequestProperty("Authorization", "key=${Constant.FIREBASE_KEY}")
            httpConnection.setRequestProperty("Content-Type", "application/json")
            httpConnection.connect()

            val body = "{\n" +
                    "  \"to\": \"/topics/${Constant.FIREBASE_TOPIC}\",\n" +
                    "  \"data\": {\n" +
                    "  \"message\": \"" + data.toString() + "\"" +
                    "  }\n" +
                    "}"
            httpConnection.outputStream.write(body.toByteArray())
            httpConnection.disconnect()
        }
    }
}
