package cz.teamnull.georgesenior

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cz.teamnull.georgesenior.ui.account.AccountFragment

class AccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, AccountFragment.newInstance())
                .commitNow()
        }
    }
}