package com.example.drivety.utils

import android.content.Context
import android.os.Vibrator
import com.example.drivety.DrivetyApplication.Companion.getContext as context

object Vibrator {

    private val vibrator by lazy { context()?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator }
    private val pattern = longArrayOf(0, 100, 1000)

    fun vibrate() {
        vibrator.vibrate(pattern, 0)
    }
}
