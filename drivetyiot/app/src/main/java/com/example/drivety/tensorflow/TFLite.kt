package com.example.drivety.tensorflow

import android.content.Context
import com.example.drivety.utils.DIM_BATCH_SIZE
import com.example.drivety.utils.DIM_IMG_SIZE_X
import com.example.drivety.utils.DIM_IMG_SIZE_Y
import com.example.drivety.utils.DIM_PIXEL_SIZE
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager
import com.google.firebase.ml.custom.*
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.coroutines.resume

/**
 * Created by ericksumargo on 01/11/19
 */

class TFLite(val context: Context) {

    private val labels by lazy {
        BufferedReader(InputStreamReader(context.resources.assets.open(LABEL_NAME))).lineSequence()
            .toList()
    }

    val modelInputOutputOptions by lazy {
        val inputDims = arrayOf(DIM_BATCH_SIZE, DIM_IMG_SIZE_X, DIM_IMG_SIZE_Y, DIM_PIXEL_SIZE)
        val outputDims = arrayOf(DIM_BATCH_SIZE, labels.size)

        FirebaseModelInputOutputOptions.Builder()
            .setInputFormat(0, FirebaseModelDataType.BYTE, inputDims.toIntArray())
            .setOutputFormat(0, FirebaseModelDataType.BYTE, outputDims.toIntArray())
            .build()
    }

    fun createLocalModelInterpreter(): FirebaseModelInterpreter {
        val localModelName =
            context.resources.assets.list("")?.firstOrNull { it.endsWith(".tflite") }
        val localModel =
            FirebaseCustomLocalModel.Builder().setAssetFilePath(localModelName.orEmpty()).build()

        val localIntepreter = FirebaseModelInterpreter.getInstance(
            FirebaseModelInterpreterOptions.Builder(localModel).build()
        )!!
        return localIntepreter
    }

    suspend fun createRemoteModelInterpreter(): FirebaseModelInterpreter {
        return suspendCancellableCoroutine { cont ->
            val remoteModel = FirebaseCustomRemoteModel.Builder(REMOTE_MODEL_NAME).build()
            val manager = FirebaseModelManager.getInstance()
            val conditions = FirebaseModelDownloadConditions.Builder().requireWifi().build()

            manager.download(remoteModel, conditions).addOnCompleteListener {
                if (it.isSuccessful) {
                    val remoteInterpreter = FirebaseModelInterpreter.getInstance(
                        FirebaseModelInterpreterOptions.Builder(remoteModel).build()
                    )!!
                    cont.resume(remoteInterpreter)
                }
            }
        }
    }

    companion object {
        private const val LABEL_NAME: String = "labels.txt"
        private const val REMOTE_MODEL_NAME: String = "mobilenet_v1_224_quant"
    }
}
