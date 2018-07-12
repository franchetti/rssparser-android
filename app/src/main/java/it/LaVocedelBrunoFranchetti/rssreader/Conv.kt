package it.LaVocedelBrunoFranchetti.rssreader

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.webkit.WebView
import android.widget.ListView
import android.widget.Toast

import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import org.xml.sax.SAXException

import java.io.BufferedInputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.ArrayList

import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException

class MainActivity : AppCompatActivity() {

    private var listView: ListView? = null
    private var progressDialog: ProgressDialog? = null
    private var modelList: MutableList<Model>? = null
    private var actionBar: ActionBar? = null
    private val context: Context? = null
    private val webView: WebView? = null

    private fun isNetworkAvailable(context: Context): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.items, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when (item.itemId) {
            R.id.who -> {
                val link = "http://istitutobrunofranchetti.gov.it/giornalino/chi-siamo"
                val intent = Intent(context, view::class.java)
                intent.putExtra("link", link)
                context!!.startActivity(intent)
            }
            R.id.mail -> {
                val i = Intent(Intent.ACTION_SEND)
                i.type = "message/rfc822"
                i.putExtra(Intent.EXTRA_EMAIL, arrayOf("emiliodallatorre12@live.com"))
                try {
                    startActivity(Intent.createChooser(i, resources.getString(R.string.intent_send)))
                } catch (ex: android.content.ActivityNotFoundException) {
                    Toast.makeText(this@MainActivity, resources.getString(R.string.intent_error), Toast.LENGTH_LONG).show()
                }

            }
            R.id.rate -> try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(resources.getString(R.string.intent_store))))
            } catch (anfe: android.content.ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(resources.getString(R.string.intent_raw_url))))
            }

        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.listView)

        HaberServisiAsynTask().execute(resources.getString(R.string.rss_link))

        actionBar = supportActionBar


        actionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM


        actionBar!!.setCustomView(R.layout.action_bar_title)

        /*        MobileAds.initialize(getApplicationContext(), "ca-app-pub-4586118376037791~8745478356");
        AdView mAdView = (AdView) this.findViewById(R.id.adViewINMAIN);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
*/
    }

    internal inner class HaberServisiAsynTask : AsyncTask<String, String, List<Model>>() {


        override fun doInBackground(vararg params: String): List<Model> {
            modelList = ArrayList()
            var connessione: HttpURLConnection? = null
            try {
                val url = URL(params[0])
                connessione = url.openConnection() as HttpURLConnection
                val baglantiDurumu = connessione.responseCode
                println(connessione)

                if (baglantiDurumu == HttpURLConnection.HTTP_OK) {

                    val bufferedInputStream = BufferedInputStream(connessione.inputStream)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
                        publishProgress("Caricamento in corso...")
                    }
                    val documentBuilderFactory = DocumentBuilderFactory.newInstance()
                    val documentBuilder = documentBuilderFactory.newDocumentBuilder()
                    val document = documentBuilder.parse(bufferedInputStream)

                    val haberNodeList = document.getElementsByTagName("item")

                    for (i in 0 until haberNodeList.length) {
                        val element = haberNodeList.item(i) as Element

                        val nodeListTitle = element.getElementsByTagName("title")
                        val nodeListLink = element.getElementsByTagName("link")
                        val nodeListDate = element.getElementsByTagName("pubDate")
                        val nodeListCreator = element.getElementsByTagName("dc:creator")
                        //                        NodeList nodeListDescription = element.getElementsByTagName("description");

                        val title = nodeListTitle.item(0).firstChild.nodeValue
                        val link = nodeListLink.item(0).firstChild.nodeValue
                        val date = nodeListDate.item(0).firstChild.nodeValue
                        val creator = nodeListCreator.item(0).firstChild.nodeValue
                        //                        String description = nodeListDescription.item(0).getFirstChild().getNodeValue();

                        /*                        String imgurl = null;
                        org.jsoup.nodes.Document documentA = null;
                        try {
                            documentA = Jsoup.connect(link).userAgent("Mozilla").get();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            org.jsoup.nodes.Element image = documentA.select("img.alignright, img.alignleft, img.aligncenter, .size-full").first();
                            imgurl = image.absUrl("src");
                        } catch (java.lang.NullPointerException e) {
                            e.printStackTrace();
                        }
                        Bitmap bitmap = null;
                        Bitmap resizedbitmap = null;
                        try {
                            URL urlResim = new URL(imgurl);
                            bitmap = BitmapFactory.decodeStream(urlResim.openConnection().getInputStream());
                            resizedbitmap = Bitmap.createScaledBitmap(bitmap, 250, 150, true);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
*/
                        val model = Model()
                        model.title = title
                        model.link = link
                        model.date = date
                        model.creator = creator
                        /*                        model.setImage(resizedbitmap);
                        model.setDescription(description);
*/
                        modelList!!.add(model)
                        publishProgress("Caricamento...")
                    }


                } else {
                    Toast.makeText(this@MainActivity,
                            "Per cortesia, controlla di essere connesso a internet.",
                            Toast.LENGTH_SHORT).show()

                }
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this@MainActivity,
                        "Errore del server internet: il sito www.istitutobrunofranchetti.gov.it/giornalino non è momentaneamente raggiungibile.",
                        Toast.LENGTH_LONG).show()
            } catch (e: ParserConfigurationException) {
                e.printStackTrace()
                Toast.makeText(this@MainActivity, "Errore del server internet: il sito www.istitutobrunofranchetti.gov.it/giornalino non è momentaneamente raggiungibile.", Toast.LENGTH_LONG).show()
            } catch (e: SAXException) {
                e.printStackTrace()
                Toast.makeText(this@MainActivity, "Errore del server internet: il sito www.istitutobrunofranchetti.gov.it/giornalino non è momentaneamente raggiungibile.", Toast.LENGTH_LONG).show()
            } finally {
                connessione?.disconnect()
            }

            return modelList

        }

        override fun onPreExecute() {
            super.onPreExecute()
            progressDialog = ProgressDialog.show(this@MainActivity,
                    "Caricamento...", "Caricamento...", true)
        }

        override fun onPostExecute(modelList: List<Model>) {
            super.onPostExecute(modelList)
            val adapter = CustomAdaptor(this@MainActivity, modelList)
            listView!!.adapter = adapter
            progressDialog!!.cancel()
        }

        override fun onProgressUpdate(vararg values: String) {
            super.onProgressUpdate(*values)
            progressDialog!!.setMessage(values[0])
        }
    }
}