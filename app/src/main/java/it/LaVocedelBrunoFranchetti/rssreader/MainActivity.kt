package it.LaVocedelBrunoFranchetti.rssreader

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.LibsBuilder
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    @Suppress("REDUNDANT_LABEL_WARNING")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)*/
        setContentView(R.layout.activity_main)


        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            // Send the mail to the specified address.
            val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + getString(R.string.org_email)))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_object))
            emailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_text))

            startActivity(Intent.createChooser(emailIntent, "Invia con..."))
        }

        firstRefresh@GlobalScope.launch {
            swiper.isRefreshing = true
            val feedRefresh = ParseFeed(this@MainActivity)

            GlobalScope.launch {
                feedRefresh.execute().get()
                swiper.isRefreshing = false
            }
        }

        swiper.setOnRefreshListener {
            val feedRefresh = ParseFeed(this@MainActivity)

            swipeRefresh@GlobalScope.launch {
                feedRefresh.execute().get()
                swiper.isRefreshing = false
            }
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            // R.id.action_settings -> true
            R.id.action_about -> {
                LibsBuilder()
                        .withAboutIconShown(true)
                        .withAboutVersionShown(true)
                        .withAboutDescription(getString(R.string.about_text))
                        .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                        .start(this)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
