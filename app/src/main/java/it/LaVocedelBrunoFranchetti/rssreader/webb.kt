package it.LaVocedelBrunoFranchetti.rssreader

import android.app.Activity
import android.content.Context
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
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

import java.io.IOException
import java.net.MalformedURLException
import java.net.URL


/**
 * @author Emilio Dalla Torre.
 */
class webb : Activity() {

    internal var policy: StrictMode.ThreadPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build()
    private val context: Context? = null
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

        val divs = Html.fromHtml(document!!.select("div.entry-content").html().replaceAll("<img.+?>", ""))
        setContentView(R.layout.articleview)

        /*        MobileAds.initialize(getApplicationContext(), "ca-app-pub-4586118376037791~8745478356");
        AdView mAdView = (AdView) this.findViewById(R.id.adViewINART);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
*/
        val art = findViewById(R.id.art) as TextView
        art.text = divs

        val titlein = findViewById(R.id.titlein) as TextView
        titlein.text = title

        val creatorin = findViewById(R.id.creatorin) as TextView
        creatorin.text = "di $creator"

        val imageView = findViewById(R.id.iw) as ImageView
        var url: URL? = null

        try {
            val image = document!!.select("img.alignright, img.alignleft, img.aligncenter, .size-full").first()
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

        val view = findViewById(R.id.view) as Button
        view.setOnClickListener {
            setContentView(R.layout.webview)
            webView = findViewById(R.id.view) as WebView
            webView!!.settings.javaScriptEnabled = false
            webView!!.settings.builtInZoomControls = true
            webView!!.loadUrl(link)
        }
        val share = findViewById(R.id.share) as Button
        share.setOnClickListener {
            val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            val shareBody = "$title di $creator $link"
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody)
            startActivity(Intent.createChooser(sharingIntent, "Condividi tramite:"))
        }
        val send = findViewById(R.id.send) as Button
        send.setOnClickListener {
            val i = Intent(Intent.ACTION_SEND)
            i.type = "message/rfc822"
            i.putExtra(Intent.EXTRA_EMAIL, arrayOf("giornalino@istitutobrunofranchetti.gov.it"))
            i.putExtra(Intent.EXTRA_SUBJECT, "Proposta di un articolo da *inserisci il tuo nome e classe*")
            i.putExtra(Intent.EXTRA_TEXT, "Salve, questa mail è stata generata dall'app del Giornalino d'Istituto, allego l'articolo che / argomento che vorrei fosse trattato in un prossimo articolo:")
            try {
                startActivity(Intent.createChooser(i, "Scegli come inviarlo:"))
            } catch (ex: android.content.ActivityNotFoundException) {
                Toast.makeText(this@webb, "Non risulta esserci alcun client di email attualmente installato su questo dispositivo.", Toast.LENGTH_LONG).show()
            }
        }

    }
}

