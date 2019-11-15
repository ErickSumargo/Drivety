package com.example.drivety.board

import android.os.Build

/**
 * Created by ericksumargo on 01/11/19
 */

object BoardPins {
    private val RASPBERRY_PI3: String = "rpi3"

    fun getSDAPin(): String {
        when (Build.BOARD) {
            RASPBERRY_PI3 -> return "I2C1"
            else -> throw IllegalArgumentException("Unsupported device")
        }
    }
}
