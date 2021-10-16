package cz.teamnull.georgesenior

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.widget.TextView

class MessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        val message = intent.getStringExtra("MESSAGE")
        val seconds = intent.getIntExtra("SECONDS", 10)
        val textViewMessage = findViewById<TextView>(R.id.textViewMessage)
        val textViewMessageSecondary = findViewById<TextView>(R.id.textViewMessageSecondary)
        textViewMessage.text = message
        object : CountDownTimer(seconds * 1000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val s = (millisUntilFinished / 1000).toInt() + 1
                textViewMessageSecondary.text = "Zpět za $s ${seconds(s)}"
            }
            override fun onFinish() {
                finish()
            }
        }.start()
    }

    private fun seconds(s: Int) = when(s) {
        1 -> "sekundu"
        2, 3, 4 -> "sekundy"
        else -> "sekund"
    }
}