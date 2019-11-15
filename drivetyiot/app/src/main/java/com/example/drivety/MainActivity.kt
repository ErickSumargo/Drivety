package com.example.drivety

import android.bluetooth.BluetoothDevice
import android.graphics.Bitmap
import android.media.ImageReader
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.drivety.bluetooth.Bluetooth
import com.example.drivety.bluetooth.BluetoothListener
import com.example.drivety.bme280.BME280
import com.example.drivety.bme280.BME280Listener
import com.example.drivety.camera.Camera
import com.example.drivety.camera.CameraListener
import com.example.drivety.data.Payload
import com.example.drivety.tensorflow.TFLite
import com.example.drivety.utils.toBitmap
import com.example.drivety.utils.toByteBuffer
import com.google.firebase.ml.custom.FirebaseModelInputs
import com.google.firebase.ml.custom.FirebaseModelInterpreter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Created by ericksumargo on 01/11/19
 */

class MainActivity : AppCompatActivity(),
    CameraListener,
    BME280Listener,
    BluetoothListener {

    private lateinit var camera: Camera
    private lateinit var bme280: BME280
    private lateinit var bluetooth: Bluetooth

    private lateinit var tFLite: TFLite
    private lateinit var modelInterpreter: FirebaseModelInterpreter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupCamera()
        setupBME280()
        setupBluetooth()

        setupTFLiteModel()
    }

    private fun setupCamera() {
        camera = Camera(applicationContext, this)
        camera.setup()
        camera.open()
    }

    private fun setupBME280() {
        bme280 = BME280(applicationContext, this)
        bme280.setup()
        bme280.registerSensors()
    }

    private fun setupBluetooth() {
        bluetooth = Bluetooth(applicationContext, this, this)
    }

    private fun setupTFLiteModel() {
        tFLite = TFLite(applicationContext)
        GlobalScope.launch(Dispatchers.IO) {
            modelInterpreter = tFLite.createLocalModelInterpreter()
            modelInterpreter = tFLite.createRemoteModelInterpreter()
        }
    }

    private fun predictActivity(bitmap: Bitmap) {
        val data = bitmap.toByteBuffer()
        val modelInput = FirebaseModelInputs.Builder().add(data).build()
        modelInterpreter.run(modelInput, tFLite.modelInputOutputOptions).continueWith {
            val prediction = it.result?.getOutput<Array<ByteArray>>(0)!!
            /**
             * 1. Do something with prediction output.
             * 2. If prediction is matched with one of categorized detection, sendNotification(prediction).
             */
        }
    }

    private fun sendNotification(prediction: Array<ByteArray>) {
        // Dummy parameters
        val images = listOf("car_outside.png", "car_inside.png")
        val title = "Title"
        val description = prediction.toString()
        val coordinates = "longitude: 0.0f" to "latitude: 0.0f"

        val payload = Payload(images, title, description, coordinates)
        /**
         * if Drivety app is in bluetooth range, bluetooth.sendNotification(payload)
         * else, FirebaseManager.sendNotification(payload)
         */
    }

    /**
     * Camera Listener
     */

    override fun onCameraAvailable() {
        Log.d(TAG, "Camera Available")
    }

    override fun onImageReady(reader: ImageReader) {
        Log.d(TAG, "Image Ready")

        val bitmap = reader.toBitmap()
        predictActivity(bitmap)
    }

    /**
     * BME280 Listener
     */

    override fun onTemperatureChanged(value: Float) {
        Log.d(TAG, "Temperature Changed: $value")
    }

    override fun onPressureChanged(value: Float) {
        Log.d(TAG, "Pressure Changed: $value")
    }

    override fun onHumidityChanged(value: Float) {
        Log.d(TAG, "Humidity Changed: $value")
    }

    /**
     * Bluetooth Listener
     */

    override fun onDiscoveryStarted() {
        Log.d(TAG, "Discovery Started")
    }

    override fun onDiscoveryFinished() {
        Log.d(TAG, "Discovery Finished")
    }

    override fun onDevicePaired(device: BluetoothDevice?) {
        Log.d(TAG, "Device Paired: $device")
    }

    override fun onDeviceUnpaired(device: BluetoothDevice?) {
        Log.d(TAG, "Device Unpaired: $device")
    }

    override fun onDeviceConnected(device: BluetoothDevice?) {
        Log.d(TAG, "Device Connected: $device")
    }

    override fun onDeviceDisconnected(device: BluetoothDevice?) {
        Log.d(TAG, "Device Disconnected: $device")
    }

    override fun onBluetoothDataReceived(bytes: ByteArray?, data: String?) {
        Log.d(TAG, "Data Received: $data")
    }

    override fun onDestroy() {
        bme280.unregisterSensors()
        super.onDestroy()
    }

    companion object {
        private val TAG: String = MainActivity::class.java.simpleName
    }
}
