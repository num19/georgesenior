package cz.teamnull.georgesenior.ui.account

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.speech.tts.Voice
import android.util.Log
import androidx.lifecycle.ViewModel
import cz.teamnull.georgesenior.InfoActivity
import cz.teamnull.georgesenior.data.SpeechParser
import cz.teamnull.georgesenior.utils.goto
import kotlinx.coroutines.Dispatchers
import java.util.*

class AccountViewModel : ViewModel() {
    var isListening = false
    private set

    private lateinit var recognizer: SpeechRecognizer
    private var synthesizer: TextToSpeech? = null
    private lateinit var intent: Intent
    var onPrepared: (() -> Unit)? = null
    var onBeginningOfSpeech: (() -> Unit)? = null
    var onEndOfSpeech: (() -> Unit)? = null
    var onFinished: (() -> Unit)? = null
    private var speaking = false
    private lateinit var timer: CountDownTimer
    private var willListenLater = false

    companion object {
        private lateinit var model: AccountViewModel
        private lateinit var act: Activity
        var balance = 42069.34
        set(value) {
            field = value
            model.balanceChanged()
        }
        fun action(s: String) {
            if (s == "receive") act.goto(InfoActivity::class.java)
        }
    }

    lateinit var balanceChanged: (() -> Unit)


    fun init(activity: Activity) {
        act = activity
        model = this
        synthesizer = TextToSpeech(activity, synthesizerListener)
        SpeechParser.output = { text ->
            Log.d("SYNTHESIZER", "says: $text")
            synthesizer?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "output")
        }
        Thread { SpeechParser.init() }.start()
        recognizer = SpeechRecognizer.createSpeechRecognizer(activity)
        intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "cs")
        recognizer.setRecognitionListener(object: RecognitionListener {
            override fun onReadyForSpeech(p0: Bundle?) {
                Log.d("RECOGNIZER", "onReadyForSpeech")
                isListening = true
                onPrepared?.let { it() }
            }

            override fun onBeginningOfSpeech() {
                Log.d("RECOGNIZER", "onBeginningOfSpeech")
                speaking = true
                onBeginningOfSpeech?.let { it() }
            }

            override fun onRmsChanged(p0: Float) {}

            override fun onBufferReceived(p0: ByteArray?) {}

            override fun onEndOfSpeech() {
                Log.d("RECOGNIZER", "onEndOfSpeech")
                speaking = false
                onEndOfSpeech?.let { it() }
            }

            override fun onError(p0: Int) {}

            override fun onResults(bundle: Bundle?) {
                Log.d("RECOGNIZER", "onResults")
                timer.cancel()
                onResults()
                val data = bundle?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.get(0)
                if (data != null) {
                    if (SpeechParser.parse(data)) return
                    willListenLater = true
                    synthesizer?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                        override fun onStart(p0: String?) { }

                        override fun onDone(p0: String?) {
                            synthesizer?.setOnUtteranceProgressListener(null)
                            Log.d("SYNTHESIZER", "onDone")
                            activity.runOnUiThread {
                                willListenLater = false
                                listen()
                            }
                        }

                        override fun onError(p0: String?) {}

                    })

                } else {
                    Log.d("RECOGNIZER", "no data")
                }
            }

            override fun onPartialResults(bundle: Bundle?) {}

            override fun onEvent(p0: Int, p1: Bundle?) {}
        })
    }

    private val synthesizerListener = TextToSpeech.OnInitListener {
        Log.d("SYNTHESIZER", "can speak: ${it == TextToSpeech.SUCCESS}")
        Log.d("SYNTHESIZER", "lang ok: ${synthesizer?.setLanguage(Locale.forLanguageTag("cs")) == TextToSpeech.LANG_AVAILABLE}")
        synthesizer?.apply {
            voice = Voice(voice.name,
                voice.locale,
                Voice.QUALITY_VERY_HIGH,
                voice.latency,
                voice.isNetworkConnectionRequired,
                voice.features)
        }
    }

    private fun onResults() {
        Log.d("RECOGNIZER", "onResults")
        isListening = false
        onFinished?.let { it() }
    }

    fun listen(){
        if (willListenLater) return
        speaking = false
        recognizer.startListening(intent)
        timer = object : CountDownTimer(3000L, 3000L) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                if (!speaking) {
                    recognizer.stopListening()
                    onResults()
                }
            }
        }
        timer.start()

    }

}