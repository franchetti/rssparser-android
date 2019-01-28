package it.LaVocedelBrunoFranchetti.rssreader

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.text.Html
import android.text.Spanned
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.webkit.WebView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import it.LaVocedelBrunoFranchetti.rssreader.R.layout.article_view
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException

class ArticleView : Activity() {
    private lateinit var link: String
    private lateinit var title: String
    private lateinit var creator: String

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(article_view)

        // TODO: Add ProgressDialog to this activity.

        GlobalScope.launch {
            link = intent.getStringExtra("link")
            title = intent.getStringExtra("title")
            creator = "di " + intent.getStringExtra("creator")

            runOnUiThread {
                findViewById<TextView>(R.id.titlein).text = title
                findViewById<TextView>(R.id.creatorin).text = creator
            }
        }
    }

    public override fun onStart() {
        super.onStart()

        // Load the article text and show it.
        GlobalScope.launch {
            try {
                val document: Document = Jsoup.connect(link).userAgent("Mozilla").get()
                document.getElementsByClass("single-line-meta").remove()
                // TODO: Remove deprecated fromHtml method.
                val divs: Spanned = Html.fromHtml(document.getElementsByTag("p").html(), ImageGetter(), null)

                runOnUiThread {
                    findViewById<TextView>(R.id.art).text = divs
                }

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        // Work with the buttons at the end of the article.
        val view = findViewById<Button>(R.id.view)
        view.setOnClickListener {
            val myWebView = WebView(applicationContext)
            setContentView(myWebView)
            myWebView.settings.javaScriptEnabled = false
            myWebView.settings.builtInZoomControls = false
            myWebView.loadUrl(link)
        }

        val share = findViewById<Button>(R.id.share)
        share.setOnClickListener {
            val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            val shareBody = "$title di $creator\n$link"
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody)
            startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_select)))
        }

        val send = findViewById<Button>(R.id.send)
        send.setOnClickListener {
            val i = Intent(Intent.ACTION_SEND)
            i.type = "message/rfc822"
            i.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.org_email)))
            i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_object))
            i.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_text))
            try {
                startActivity(Intent.createChooser(i, getString(R.string.share_select)))
            } catch (ex: android.content.ActivityNotFoundException) {
                Toast.makeText(this@ArticleView, getString(R.string.error_noclient), Toast.LENGTH_LONG).show()
            }
        }
    }
}