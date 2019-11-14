package com.example.drivety.bluetooth

import android.bluetooth.BluetoothDevice

/**
 * Created by ericksumargo on 01/11/19
 */

interface BluetoothListener {
    fun onDiscoveryStarted()
    fun onDiscoveryFinished()
    fun onDevicePaired(device: BluetoothDevice?)
    fun onDeviceUnpaired(device: BluetoothDevice?)
    fun onDeviceConnected(device: BluetoothDevice?)
    fun onDeviceDisconnected(device: BluetoothDevice?)
    fun onBluetoothDataReceived(bytes: ByteArray?, data: String?)
}