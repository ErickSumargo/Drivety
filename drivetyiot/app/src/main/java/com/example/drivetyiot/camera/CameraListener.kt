package com.example.drivetyiot.camera

import android.media.ImageReader

interface CameraListener {
    fun onCameraAvailable()
    fun onImageReady(reader: ImageReader)
}