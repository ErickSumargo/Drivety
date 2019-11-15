package com.example.drivety

import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.devpaul.bluetoothutillib.dialogs.DeviceDialog
import com.example.drivety.bluetooth.Bluetooth
import com.example.drivety.bluetooth.BluetoothListener
import com.example.drivety.data.Payload
import com.example.drivety.speech.SpeechRecognition
import com.example.drivety.utils.Constant.REQUEST_CODE_BLUETOOTH_SCAN
import com.example.drivety.utils.NotificationManager
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by ericksumargo on 01/11/19
 */

class MainActivity : AppCompatActivity(),
    BluetoothListener,
    RecognitionListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    private lateinit var bluetooth: Bluetooth
    private lateinit var speechRecognition: SpeechRecognition

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        setupActionBar()

        setupBluetooth()
        setupSpeechRecognition()
    }

    private fun setupBluetooth() {
        bluetooth = Bluetooth(applicationContext, this, this)
        bluetooth.scan(REQUEST_CODE_BLUETOOTH_SCAN)
    }

    private fun setupSpeechRecognition() {
        speechRecognition = SpeechRecognition(applicationContext, this)
        speechRecognition.setup()
    }

    /**
     * Support actionBar with fragment name
     */

    private fun setupActionBar() {
        navHostFragment = navHost as? NavHostFragment ?: return
        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    /**
     * Support navigate up
     */

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.navHost).navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_BLUETOOTH_SCAN -> {
                    val macAddress =
                        data?.getStringExtra(DeviceDialog.DEVICE_DIALOG_DEVICE_ADDRESS_EXTRA)
                    bluetooth.connect(macAddress.orEmpty())
                }
            }
        }
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
        val payload = GsonBuilder().create().fromJson(data, Payload::class.java)

        // Handle payload data.
        Log.d(TAG, "Data Received: $data")
        NotificationManager.build(payload)
    }

    /**
     * Speech Recognition Listener
     */

    override fun onReadyForSpeech(params: Bundle?) {
        Log.d(TAG, "Ready for Speech")
    }

    override fun onRmsChanged(rmsdB: Float) {
        Log.d(TAG, "RMS Changed")
    }

    override fun onBufferReceived(buffer: ByteArray?) {
        Log.d(TAG, "Buffer Received")
    }

    override fun onPartialResults(partialResults: Bundle?) {
        Log.d(TAG, "Partial Results")
    }

    override fun onEvent(eventType: Int, params: Bundle?) {
        Log.d(TAG, "Event: $eventType")
    }

    override fun onBeginningOfSpeech() {
        Log.d(TAG, "Beginning of Speech")
    }

    override fun onEndOfSpeech() {
        Log.d(TAG, "End of Speech")
    }

    override fun onError(error: Int) {
        Log.d(TAG, "Error: $error")
    }

    override fun onResults(results: Bundle?) {
        val speech = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.get(0)

        // Handle speech data...
        Log.d(TAG, "Results: $speech")
    }

    companion object {
        private val TAG: String = MainActivity::class.java.simpleName
    }
}
