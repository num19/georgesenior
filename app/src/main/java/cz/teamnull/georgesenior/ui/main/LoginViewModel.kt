package cz.teamnull.georgesenior.ui.main

import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    fun check() = pin == "2021"

    var pin = ""
}