package com.example.drivety.bme280

import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import com.example.drivety.board.BoardPins
import com.google.android.things.contrib.driver.bmx280.Bmx280SensorDriver

/**
 * Created by ericksumargo on 01/11/19
 */

class BME280(
    val context: Context,
    val listener: BME280Listener
) {
    private lateinit var sensorManager: SensorManager
    private lateinit var sensorDriver: Bmx280SensorDriver

    fun setup() {
        sensorManager = context.getSystemService(SENSOR_SERVICE) as SensorManager
        sensorManager.registerDynamicSensorCallback(sensorCallback)
    }

    fun registerSensors() {
        sensorDriver = Bmx280SensorDriver(BoardPins.getSDAPin())
        sensorDriver.registerTemperatureSensor()
        sensorDriver.registerPressureSensor()
        sensorDriver.registerHumiditySensor()
    }

    fun unregisterSensors() {
        sensorDriver.unregisterTemperatureSensor()
        sensorDriver.unregisterPressureSensor()
        sensorDriver.unregisterHumiditySensor()
    }

    private val sensorCallback = object : SensorManager.DynamicSensorCallback() {

        override fun onDynamicSensorConnected(sensor: Sensor?) {
            super.onDynamicSensorConnected(sensor)
            Log.d(TAG, "Sensor Connected")

            when (sensor?.type) {
                Sensor.TYPE_AMBIENT_TEMPERATURE -> {
                    sensorManager.registerListener(
                        temperatureCallback,
                        sensor,
                        SensorManager.SENSOR_DELAY_NORMAL
                    )
                }
                Sensor.TYPE_PRESSURE -> {
                    sensorManager.registerListener(
                        pressureCallback,
                        sensor,
                        SensorManager.SENSOR_DELAY_NORMAL
                    )
                }
                Sensor.TYPE_RELATIVE_HUMIDITY -> {
                    sensorManager.registerListener(
                        humidityCallback,
                        sensor,
                        SensorManager.SENSOR_DELAY_NORMAL
                    )
                }
            }
        }

        override fun onDynamicSensorDisconnected(sensor: Sensor?) {
            super.onDynamicSensorDisconnected(sensor)
            Log.d(TAG, "Sensor Disconnected")
        }
    }

    private val temperatureCallback = object : SensorEventListener {

        override fun onSensorChanged(event: SensorEvent?) {
            val value = event?.values?.firstOrNull()
            listener.onTemperatureChanged(value ?: -1f)
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            Log.d(TAG, "Temperature Accuracy Changed: $accuracy")
        }
    }

    private val pressureCallback = object : SensorEventListener {

        override fun onSensorChanged(event: SensorEvent?) {
            val value = event?.values?.firstOrNull()
            listener.onPressureChanged(value ?: -1f)
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            Log.d(TAG, "Pressure Accuracy Changed: $accuracy")
        }
    }

    private val humidityCallback = object : SensorEventListener {

        override fun onSensorChanged(event: SensorEvent?) {
            val value = event?.values?.firstOrNull()
            listener.onHumidityChanged(value ?: -1f)
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            Log.d(TAG, "Humididty Accuracy Changed: $accuracy")
        }
    }

    companion object {
        private val TAG: String = BME280::class.java.simpleName
    }
}
