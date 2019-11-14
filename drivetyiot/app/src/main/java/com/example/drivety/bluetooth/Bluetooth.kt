package com.example.drivety.bluetooth

import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.util.Log
import com.devpaul.bluetoothutillib.SimpleBluetooth
import com.devpaul.bluetoothutillib.utils.BluetoothUtility
import com.devpaul.bluetoothutillib.utils.SimpleBluetoothListener

/**
 * Created by ericksumargo on 01/11/19
 */

class Bluetooth(
    val context: Context,
    val activity: Activity,
    val listener: BluetoothListener
) {
    private val TAG: String = Bluetooth::class.java.simpleName

    fun build(): SimpleBluetooth {
        val connection = SimpleBluetooth(context, activity)
        connection.setSimpleBluetoothListener(bluetoothListener)
        connection.makeDiscoverable(DURATION)
        connection.initializeSimpleBluetooth()
        connection.setInputStreamType(BluetoothUtility.InputStreamType.NORMAL)

        return connection
    }

    private val bluetoothListener = object : SimpleBluetoothListener() {

        override fun onDiscoveryStarted() {
            super.onDiscoveryStarted()
            Log.d(TAG, "Discovery Started")

            listener.onDiscoveryStarted()
        }

        override fun onDiscoveryFinished() {
            super.onDiscoveryFinished()
            Log.d(TAG, "Discovery Finished")

            listener.onDiscoveryFinished()
        }

        override fun onDevicePaired(device: BluetoothDevice?) {
            super.onDevicePaired(device)
            Log.d(TAG, "Device Paired: $device")

            listener.onDevicePaired(device)
        }

        override fun onDeviceUnpaired(device: BluetoothDevice?) {
            super.onDeviceUnpaired(device)
            Log.d(TAG, "Device Unpaired: $device")

            listener.onDeviceUnpaired(device)
        }

        override fun onDeviceConnected(device: BluetoothDevice?) {
            super.onDeviceConnected(device)
            Log.d(TAG, "Device Connected: $device")

            listener.onDeviceConnected(device)
        }

        override fun onDeviceDisconnected(device: BluetoothDevice?) {
            super.onDeviceDisconnected(device)
            Log.d(TAG, "Device Disconnected: $device")

            listener.onDeviceDisconnected(device)
        }

        override fun onBluetoothDataReceived(bytes: ByteArray?, data: String?) {
            super.onBluetoothDataReceived(bytes, data)
            Log.d(TAG, "Data Received")

            listener.onBluetoothDataReceived(bytes, data)
        }
    }

    companion object {
        const val DURATION: Int = 600
    }
}
