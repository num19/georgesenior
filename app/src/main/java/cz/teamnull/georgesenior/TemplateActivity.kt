package cz.teamnull.georgesenior

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cz.teamnull.georgesenior.ui.template.TemplateFragment

class TemplateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.template_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, TemplateFragment.newInstance())
                .commitNow()
        }
    }
}