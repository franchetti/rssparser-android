package it.LaVocedelBrunoFranchetti.rssreader

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.webkit.WebView
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
        setContentView(R.layout.article_view)

        link = intent.getStringExtra("link")
        title = intent.getStringExtra("title")
        creator = intent.getStringExtra("creator")

        // TODO: Add ProgressDialog to this activity.
    }

    /**
     * Abbiamo disattivato l'errore di JavaScript perché il sito a cui si collega l'app è sempre affidabile.
     */
    @SuppressLint("SetJavaScriptEnabled")

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
                    val webView: WebView = findViewById(R.id.articleview_webview)
                    webView.settings.javaScriptEnabled = true
                    webView.loadDataWithBaseURL(null, document.html().toString(), "text/html", "utf-8", null)
                }

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}