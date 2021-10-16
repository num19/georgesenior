package cz.teamnull.georgesenior

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cz.teamnull.georgesenior.ui.main.LoginFragment

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        window.addFlags(128)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, LoginFragment.newInstance())
                .commitNow()
        }
    }
}