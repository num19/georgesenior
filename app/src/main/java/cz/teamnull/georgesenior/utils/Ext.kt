package cz.teamnull.georgesenior.utils

import android.app.Activity
import android.content.Intent
import cz.teamnull.georgesenior.MessageActivity

fun String.pop() = if (this.isEmpty()) "" else this.dropLast(1)

fun Activity?.goto(c: Class<*>) = this?.startActivity(Intent(this, c))
fun Activity?.gotoAndFinish(c: Class<*>) {
    this?.startActivity(Intent(this, c))
    this?.finish()
}

fun Activity?.showMessage(message: String, seconds: Int) {
    val intent = Intent(this, MessageActivity::class.java)
    intent.putExtra("MESSAGE", message)
    intent.putExtra("SECONDS", seconds)
    this?.startActivity(intent)
}