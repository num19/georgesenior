package cz.teamnull.georgesenior

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.SpeechRecognizer
import androidx.core.app.ActivityCompat

import android.os.Build
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var recognizer: SpeechRecognizer
    lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (!checkPermission()) return
        recognizer = SpeechRecognizer.createSpeechRecognizer(this)
        textView = findViewById(R.id.textView)

    }

    fun onClickRecognize(v: View) {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        //intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.forLanguageTag("cs-CZ"))
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "cs")
        recognizer.setRecognitionListener(object: RecognitionListener {
            override fun onReadyForSpeech(p0: Bundle?) {

            }

            override fun onBeginningOfSpeech() {
                snack("listening")
            }

            override fun onRmsChanged(p0: Float) {

            }

            override fun onBufferReceived(p0: ByteArray?) {

            }

            override fun onEndOfSpeech() {

            }

            override fun onError(p0: Int) {

            }

            override fun onResults(bundle: Bundle?) {
                val data = bundle?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.get(0)
                textView.text = data
            }

            override fun onPartialResults(bundle: Bundle?) {
                val data = bundle?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.get(0)
                textView.text = data
            }

            override fun onEvent(p0: Int, p1: Bundle?) {

            }

        })
        recognizer.startListening(intent)
    }

    private fun snack(message: String) = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG).show()

    private fun checkPermission() = if(ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 1)
        false
    } else {
        true
    }
}