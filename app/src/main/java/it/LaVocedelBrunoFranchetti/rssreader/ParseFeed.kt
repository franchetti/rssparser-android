package it.LaVocedelBrunoFranchetti.rssreader

import android.app.Activity
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.widget.ListView
import org.w3c.dom.Element
import java.io.BufferedInputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory

class ParseFeed(context: Context) : AsyncTask<Void, Void, ArrayList<Model>>() {
    private val TAG: String = "AsyncTask"
    val modelList = ArrayList<Model>()
    // private val contexto: WeakReference<Context> = WeakReference(context)
    private val contexto: Context = context

    override fun doInBackground(vararg params: Void?): ArrayList<Model> {
        val rssLink = "http://istitutobrunofranchetti.gov.it/giornalino/feed/"
        val url = URL(rssLink)
        val connection = url.openConnection() as HttpURLConnection
        val bufferedInputStream = BufferedInputStream(connection.inputStream)
        val documentBuilderFactory = DocumentBuilderFactory.newInstance()
        val documentBuilder = documentBuilderFactory.newDocumentBuilder()
        val document = documentBuilder.parse(bufferedInputStream)
        val itemList = document.getElementsByTagName("item")

        for (i in 0 until itemList.length) {
            // Declare new model storage.
            val model = Model()

            // Get element of RSS stream.
            val element = itemList.item(i) as Element

            // Define elements of Model.
            val nodeListTitle = element.getElementsByTagName("title")
            val nodeListLink = element.getElementsByTagName("link")
            val nodeListDate = element.getElementsByTagName("pubDate")
            val nodeListCreator = element.getElementsByTagName("dc:creator")
            val nodeListDescription = element.getElementsByTagName("description")
            /* TODO: add interaction with category and comments.
            val nodeListComment = element.getElementsByTagName("slash:comments")
            val nodeListCategory = element.getElementsByTagName("category") */

            val title = nodeListTitle.item(0).firstChild.nodeValue
            val link = nodeListLink.item(0).firstChild.nodeValue
            val date = nodeListDate.item(0).firstChild.nodeValue
            val creator = nodeListCreator.item(0).firstChild.nodeValue
            val description = nodeListDescription.item(0).firstChild.nodeValue
            /* TODO: add interaction with category and comments.
            val comment = nodeListComment.item(0).firstChild.nodeValue
            val category = nodeListCategory.item(0).firstChild.nodeValue */

            // Define local Model() to be added into ArrayList.
            model.title = title
            model.link = link
            model.date = date
            model.creator = creator
            model.description = description

            // Add Model() instance to modelList ArrayList.
            modelList.add(model)
        }
        Log.d(TAG, "Parsed all the ${itemList.length} articles correctly.")
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
