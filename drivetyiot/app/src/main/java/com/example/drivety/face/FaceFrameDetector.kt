package com.example.drivety.face

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.face.FaceDetector

class FaceFrameDetector(val context: Context) {

    private lateinit var canvas: Canvas
    private lateinit var _bitmap: Bitmap

    private val faceDetector by lazy {
        FaceDetector.Builder(context)
            .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
            .build()
    }

    private val paint by lazy {
        Paint().apply {
            strokeWidth = 5f
            color = Color.RED
            style = Paint.Style.STROKE
        }
    }

    private fun setup(bitmap: Bitmap) {
        _bitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.RGB_565)

        canvas = Canvas(_bitmap)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
    }

    fun detect(bitmap: Bitmap): List<Bitmap> {
        setup(bitmap)

        val frame = Frame.Builder().setBitmap(bitmap).build()
        val detectedFaces = faceDetector.detect(frame)

        val faces = mutableListOf<Bitmap>()
        faces.add(_bitmap)
        for (i in 0 until detectedFaces.size()) {
            val detectedFace = detectedFaces.valueAt(i)
            val x1 = detectedFace.position.x
            val y1 = detectedFace.position.y
            val x2 = x1 + detectedFace.width
            val y2 = y1 + detectedFace.height

            canvas.drawRoundRect(RectF(x1, y1, x2, y2), 2f, 2f, paint)

            /**
             * Crop the face
             */
            faces.add(
                cropFace(
                    BitmapDrawable(context.resources, _bitmap).bitmap,
                    x1, y1, x2, y2
                )
            )
        }
        return faces
    }

    private fun cropFace(
        bitmap: Bitmap,
        x1: Float,
        y1: Float,
        x2: Float,
        y2: Float
    ): Bitmap {
        return Bitmap.createBitmap(
            bitmap,
            x1.toInt(),
            y1.toInt(),
            x2.toInt(),
            y2.toInt()
        )
    }
}
