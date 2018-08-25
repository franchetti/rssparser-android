package it.LaVocedelBrunoFranchetti.rssreader

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.text.Html
import android.text.Spanned
import android.view.View
import android.webkit.WebView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import it.LaVocedelBrunoFranchetti.rssreader.R.layout.article_view
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException

class ArticleView : Activity() {
    private var policy: StrictMode.ThreadPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StrictMode.setThreadPolicy(policy)
        setContentView(article_view)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        // TODO: Add ProgressDialog to this activity.

        val link = intent.getStringExtra("link")
        val title = intent.getStringExtra("title")
        val creator = "di " + intent.getStringExtra("creator")
        val document: Document
        val divs: Spanned

        try {
            document = Jsoup.connect(link).userAgent("Mozilla").get()
            document.getElementsByClass("single-line-meta").remove()
            // TODO: Remove deprecated fromHtml method.
            divs = Html.fromHtml(document.getElementsByTag("p").html(), ImageGetter(), null)

            findViewById<TextView>(R.id.titlein).text = title
            findViewById<TextView>(R.id.creatorin).text = creator
            findViewById<TextView>(R.id.art).text = divs

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
                val shareBody = title + getString(R.string.article_author_linker) + creator + "\n" + link
                sharingIntent.type = "text/plain"
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody)
                startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_select)))
            }

            val send = findViewById<Button>(R.id.send)
            send.setOnClickListener {
                try {
                    val i = Intent(Intent.ACTION_SEND)
                    i.type = "message/rfc822"
                    i.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.org_email)))
                    i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_object))
                    i.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_text))
                    startActivity(Intent.createChooser(i, getString(R.string.share_select)))
                } catch (ex: android.content.ActivityNotFoundException) {
                    Toast.makeText(this@ArticleView, getString(R.string.error_noclient), Toast.LENGTH_LONG).show()
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}