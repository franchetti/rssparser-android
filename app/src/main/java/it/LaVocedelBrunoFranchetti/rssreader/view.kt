package it.LaVocedelBrunoFranchetti.rssreader

import android.app.Activity
import android.os.Bundle
import android.webkit.WebView

/**
 * Created by Emilio Dalla Torre on 22/03/2017.
 */

class view : Activity() {

    private var webView: WebView? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.webview)
        webView = findViewById<View>(R.id.view)
        webView!!.settings.javaScriptEnabled = false
        webView!!.settings.builtInZoomControls = true
        val link = intent.getStringExtra("link")
        webView!!.loadUrl(link)
    }

}