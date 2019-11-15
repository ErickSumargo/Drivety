package com.example.drivety.speech

import android.content.Context
import android.content.Intent
import android.speech.RecognitionListener
import android.speech.RecognizerIntent.*
import android.speech.SpeechRecognizer

/**
 * Created by ericksumargo on 01/11/19
 */

class SpeechRecognition(
    val context: Context,
    val listener: RecognitionListener
) {

    fun setup() {
        val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
        speechRecognizer.setRecognitionListener(listener)

        val speechIntent = Intent(ACTION_RECOGNIZE_SPEECH)
        speechIntent.putExtra(EXTRA_LANGUAGE_MODEL, LANGUAGE_MODEL_FREE_FORM)
        speechIntent.putExtra(EXTRA_CALLING_PACKAGE, context.packageName)
        speechRecognizer.startListening(speechIntent)
    }
}
