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
    private var policy: StrictMode.ThreadPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(article_view)

        link = intent.getStringExtra("link")
        title = intent.getStringExtra("title")
        creator = intent.getStringExtra("creator")

        // TODO: Add ProgressDialog to this activity.
    }

    public override fun onStart() {
        super.onStart()

        // Load the article text and show it.
        GlobalScope.launch {
            try {
                val document: Document = Jsoup.connect(link).userAgent("Mozilla/5.0 (Android 4.4; Mobile; rv:41.0) Gecko/41.0 Firefox/41.0").get()
                document.getElementsByClass("single-line-meta").remove()
                document.getElementById("masthead").remove()
                document.getElementsByClass("si-share").remove()
                document.getElementsByClass("nav-single").remove()
                document.getElementsByClass("comments-area").remove()
                document.getElementById("secondary").remove()
                document.getElementsByClass("social col-md-6").remove()

                runOnUiThread {
                    findViewById<WebView>(R.id.articleview_webview).loadDataWithBaseURL(null, document.html().toString(), "text/html", "utf-8", null)
                }

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}