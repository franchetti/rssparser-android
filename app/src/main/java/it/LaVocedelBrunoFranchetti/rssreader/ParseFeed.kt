package it.LaVocedelBrunoFranchetti.rssreader

import android.app.Activity
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.widget.ListView
import org.jsoup.Jsoup
import org.w3c.dom.Element
import java.io.BufferedInputStream
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory

class ParseFeed(context: Context) : AsyncTask<Void, Void, ArrayList<Model>>() {
    private val TAG: String = "AsyncTask"
    val modelList = ArrayList<Model>()
    // private val contexto: WeakReference<Context> = WeakReference(context)
    private val contexto: Context = context

    override fun doInBackground(vararg params: Void?): ArrayList<Model> {
        val rssLink = "http://istitutobrunofranchetti.gov.it/giornalino/feed/"
        val itemList = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(BufferedInputStream(URL(rssLink).openConnection().inputStream)).getElementsByTagName("item")

        for (i in 0 until 15) {
            // Declare new model storage.
            val model = Model()

            // Get element of RSS stream.
            val element = itemList.item(i) as Element

            /* TODO: add interaction with category and comments.
            val nodeListComment = element.getElementsByTagName("slash:comments")
            val nodeListCategory = element.getElementsByTagName("category") */

            /* TODO: add interaction with category and comments.
            val comment = nodeListComment.item(0).firstChild.nodeValue
            val category = nodeListCategory.item(0).firstChild.nodeValue */

            // Define local Model() to be added into ArrayList.
            model.title = element.getElementsByTagName("title").item(0).firstChild.nodeValue
            model.link = element.getElementsByTagName("link").item(0).firstChild.nodeValue
            model.date = element.getElementsByTagName("pubDate").item(0).firstChild.nodeValue
            model.creator = element.getElementsByTagName("dc:creator").item(0).firstChild.nodeValue
            model.description = Jsoup.parse(element.getElementsByTagName("description").item(0).firstChild.nodeValue).text().replace(" Read More", "...")

            // Add Model() instance to modelList ArrayList.
            modelList.add(model)
        }
        Log.d(TAG, "Parsed all the ${modelList.size} articles correctly.")
        return modelList
    }

    override fun onPreExecute() {
        super.onPreExecute()
        // TODO: create ProgressBar.
    }

    override fun onPostExecute(modelList: ArrayList<Model>) {
        super.onPostExecute(modelList)
        (contexto as Activity).findViewById<ListView>(R.id.listView).adapter = CustomAdapter(contexto, modelList)
    }
}
