package com.example.drivety

import android.app.Application
import com.example.drivety.utils.Constant
import com.google.firebase.messaging.FirebaseMessaging

/**
 * Created by ericksumargo on 01/11/19
 */

class DrivetyApplication : Application() {

    init {
        application = this
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseMessaging.getInstance().subscribeToTopic(Constant.FIREBASE_TOPIC)
    }

    companion object {
        private var application: DrivetyApplication? = null

        fun getContext() = application?.applicationContext
    }
}