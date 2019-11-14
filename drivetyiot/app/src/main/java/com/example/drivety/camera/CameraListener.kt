package com.example.drivety.camera

import android.media.ImageReader

/**
 * Created by ericksumargo on 01/11/19
 */

interface CameraListener {
    fun onCameraAvailable()
    fun onImageReady(reader: ImageReader)
}