package com.example.drivety.bluetooth

import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.content.Context
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

    private val bluetoothConnection by lazy {
        val connection = SimpleBluetooth(context, activity)
        connection.setSimpleBluetoothListener(bluetoothCallback)
        connection.makeDiscoverable(DURATION)
        connection.initializeSimpleBluetooth()
        connection.setInputStreamType(BluetoothUtility.InputStreamType.NORMAL)
        connection
    }

    fun scan(requestCode: Int) {
        // bluetoothConnection.scan(requestCode)
    }

    fun connect(macAddress: String) {
        bluetoothConnection.connectToBluetoothDevice(macAddress)
    }

    private val bluetoothCallback = object : SimpleBluetoothListener() {

        override fun onDiscoveryStarted() {
            super.onDiscoveryStarted()
            listener.onDiscoveryStarted()
        }

        override fun onDiscoveryFinished() {
            super.onDiscoveryFinished()
            listener.onDiscoveryFinished()
        }

        override fun onDevicePaired(device: BluetoothDevice?) {
            super.onDevicePaired(device)
            listener.onDevicePaired(device)
        }

        override fun onDeviceUnpaired(device: BluetoothDevice?) {
            super.onDeviceUnpaired(device)
            listener.onDeviceUnpaired(device)
        }

        override fun onDeviceConnected(device: BluetoothDevice?) {
            super.onDeviceConnected(device)
            listener.onDeviceConnected(device)
        }

        override fun onDeviceDisconnected(device: BluetoothDevice?) {
            super.onDeviceDisconnected(device)
            listener.onDeviceDisconnected(device)
        }

        override fun onBluetoothDataReceived(bytes: ByteArray?, data: String?) {
            super.onBluetoothDataReceived(bytes, data)
            listener.onBluetoothDataReceived(bytes, data)
        }
    }

    companion object {
        private const val DURATION: Int = 600
    }
}
