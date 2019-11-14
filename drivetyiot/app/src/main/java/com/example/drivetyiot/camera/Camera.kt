package com.example.drivetyiot.camera

import android.content.Context
import android.graphics.ImageFormat
import android.hardware.camera2.*
import android.media.ImageReader
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import java.util.*

/**
 * Created by ericksumargo on 01/11/19
 */

class Camera {
    private val TAG: String = Camera::class.java.simpleName

    private var id: String? = null
    private val handler: HandlerThread = HandlerThread("")

    private lateinit var context: Context
    private lateinit var camera: CameraDevice
    private lateinit var manager: CameraManager
    private lateinit var session: CameraCaptureSession
    private lateinit var listener: CameraListener
    private lateinit var reader: ImageReader

    fun Camera(context: Context, listener: CameraListener) {
        this.context = context
        this.listener = listener
    }

    fun setup() {
        // Initialize CameraManager
        manager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager

        // Initialize ImageReader
        handler.start()

        reader = ImageReader.newInstance(WIDTH, HEIGHT, ImageFormat.JPEG, MAX_IMAGES)
        reader.setOnImageAvailableListener({ reader ->
            listener.onImageReady(reader)
        }, Handler(handler.looper))

        // Retrieve id of camera
        id = manager.cameraIdList.firstOrNull()
    }

    fun open() {
        manager.openCamera(id.orEmpty(), cameraCallback, null)
    }

    fun startCapturePicture() {
        val builder = camera.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
        builder.addTarget(reader.surface)
        builder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON)

        session.capture(builder.build(), captureCallback, null)
    }

    fun takePicture() {
        camera.createCaptureSession(
            Collections.singletonList(reader.surface),
            sessionCallback,
            null
        )
    }

    private val cameraCallback = object : CameraDevice.StateCallback() {

        override fun onOpened(camera: CameraDevice) {
            Log.d(TAG, "Camera Opened")

            this@Camera.camera = camera
            listener.onCameraAvailable()
        }

        override fun onDisconnected(camera: CameraDevice) {
            Log.d(TAG, "Camera Disconnected")
        }

        override fun onError(camera: CameraDevice, error: Int) {
            Log.d(TAG, "Camera Error: $error")
        }
    }

    private val sessionCallback = object : CameraCaptureSession.StateCallback() {

        override fun onConfigured(session: CameraCaptureSession) {
            Log.d(TAG, "Camera configured")

            this@Camera.session = session
            startCapturePicture()
        }

        override fun onConfigureFailed(session: CameraCaptureSession) {
            Log.e(TAG, "Configuration Failed")
        }
    }

    private val captureCallback = object : CameraCaptureSession.CaptureCallback() {
        override fun onCaptureCompleted(
            session: CameraCaptureSession,
            request: CaptureRequest,
            result: TotalCaptureResult
        ) {
            Log.d(TAG, "Camera Completed")
            session.close()
        }
    }

    companion object {
        const val WIDTH: Int = 960
        const val HEIGHT: Int = 480
        const val MAX_IMAGES: Int = 1
    }
}
