package cz.teamnull.georgesenior

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cz.teamnull.georgesenior.ui.info.InfoFragment

class InfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.info_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, InfoFragment.newInstance())
                .commitNow()
        }
    }
}