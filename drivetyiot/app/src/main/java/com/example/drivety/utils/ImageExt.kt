package com.example.drivety.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ImageReader
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * Created by ericksumargo on 01/11/19
 */

fun ImageReader.toBitmap(): Bitmap {
    val image = this.acquireLatestImage()
    val buffer = image.planes[0].buffer
    val bytes = ByteArray(buffer.remaining())

    buffer.get(bytes)
    image.close()

    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}

fun Bitmap.toByteBuffer(): ByteBuffer {
    val imageBuffer = IntArray(DIM_IMG_SIZE_X * DIM_IMG_SIZE_Y)

    val byteBuffer =
        ByteBuffer.allocateDirect(DIM_BATCH_SIZE * DIM_IMG_SIZE_X * DIM_IMG_SIZE_Y * DIM_PIXEL_SIZE)
    byteBuffer.order(ByteOrder.nativeOrder())
    byteBuffer.rewind()

    val scaledBitmap = Bitmap.createScaledBitmap(this, DIM_IMG_SIZE_X, DIM_IMG_SIZE_Y, true)
    scaledBitmap.getPixels(
        imageBuffer,
        0,
        scaledBitmap.width,
        0,
        0,
        scaledBitmap.width,
        scaledBitmap.height
    )

    var position = 0
    for (i in 0 until DIM_IMG_SIZE_X) {
        for (j in 0 until DIM_IMG_SIZE_Y) {
            val pixel = imageBuffer[position++]
            byteBuffer.put((pixel shr 16 and 0xFF).toByte())
            byteBuffer.put((pixel shr 8 and 0xFF).toByte())
            byteBuffer.put((pixel and 0xFF).toByte())
        }
    }
    return byteBuffer
}

const val DIM_BATCH_SIZE = 1
const val DIM_PIXEL_SIZE = 3
const val DIM_IMG_SIZE_X = 960
const val DIM_IMG_SIZE_Y = 480