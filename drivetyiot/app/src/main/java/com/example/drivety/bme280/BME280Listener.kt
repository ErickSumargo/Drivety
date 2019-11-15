package com.example.drivety.bme280

/**
 * Created by ericksumargo on 01/11/19
 */

interface BME280Listener {
    fun onTemperatureChanged(value: Float)
    fun onPressureChanged(value: Float)
    fun onHumidityChanged(value: Float)
}