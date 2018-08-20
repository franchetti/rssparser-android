package it.LaVocedelBrunoFranchetti.rssreader

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.StrictMode
import android.text.Html
import android.webkit.WebView
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import it.LaVocedelBrunoFranchetti.rssreader.R.layout.article_view
import it.LaVocedelBrunoFranchetti.rssreader.R.layout.webview
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

class ArticleView : Activity() {

    internal var policy: StrictMode.ThreadPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build()
    private var webView: WebView? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val link = intent.getStringExtra("link")
        val title = intent.getStringExtra("title")
        val creator = intent.getStringExtra("creator")
        StrictMode.setThreadPolicy(policy)
        var document: Document? = null

        try {
            document = Jsoup.connect(link).userAgent("Mozilla").get()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val divs = Html.fromHtml(document!!.select("div.entry-content").html().replace("<img.+?>", ""))
        setContentView(article_view)

        val art = findViewById<TextView>(R.id.art)
        art.text = divs
 /*
        val titlein = findViewById<TextView>(R.id.titlein)
        titlein.text = title

        val creatorin = findViewById<TextView>(R.id.creatorin)
        creatorin.text = "di $creator"
        */

        val imageView = findViewById<ImageView>(R.id.iw)
        var url: URL? = null

        try {
            val image = document.select("img.alignright, img.alignleft, img.aligncenter, .size-full").first()
            val imgurl = image.absUrl("src")
            try {
                url = URL(imgurl)
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            }

            var bmp: Bitmap? = null
            try {
                bmp = BitmapFactory.decodeStream(url!!.openConnection().getInputStream())
            } catch (e: IOException) {
                e.printStackTrace()
            }

            imageView.setImageBitmap(bmp)
        } catch (e: java.lang.NullPointerException) {
            e.printStackTrace()
            val text = "Questo articolo non contiene immagini o quelle presenti potrebbero non essere supportate da questa app. Per visualizzare completamente l'articolo, visita: "
            Toast.makeText(this, text + link, Toast.LENGTH_LONG).show()
        }

        val view = findViewById<Button>(R.id.view)
        view.setOnClickListener {
            setContentView(R.layout.webview)
            webView = webview as WebView
            webView!!.settings.javaScriptEnabled = false
            webView!!.settings.builtInZoomControls = true
            webView!!.loadUrl(link)
        }
        val share = findViewById<Button>(R.id.share)
        share.setOnClickListener {
            val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            val shareBody = "$title di $creator $link"
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody)
            startActivity(Intent.createChooser(sharingIntent, "Condividi tramite:"))
        }
        val send = findViewById<Button>(R.id.send)
        send.setOnClickListener {
            val i = Intent(Intent.ACTION_SEND)
            i.type = "message/rfc822"
            i.putExtra(Intent.EXTRA_EMAIL, arrayOf("giornalino@istitutobrunofranchetti.gov.it"))
            i.putExtra(Intent.EXTRA_SUBJECT, "Proposta di un articolo da *inserisci il tuo nome e classe*")
            i.putExtra(Intent.EXTRA_TEXT, "Salve, questa mail è stata generata dall'app del Giornalino d'Istituto, allego l'articolo che / argomento che vorrei fosse trattato in un prossimo articolo:")

            try {
                startActivity(Intent.createChooser(i, "Scegli come inviarlo:"))
            } catch (ex: android.content.ActivityNotFoundException) {
                Toast.makeText(this@ArticleView, "Non risulta esserci alcun client di email attualmente installato su questo dispositivo.", Toast.LENGTH_LONG).show()
            }
        }

    }
}

