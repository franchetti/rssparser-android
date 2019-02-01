package it.LaVocedelBrunoFranchetti.rssreader

import android.app.Activity
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.widget.ListView
import android.widget.ProgressBar
import org.jsoup.Jsoup
import org.w3c.dom.Element
import java.io.BufferedInputStream
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory

class ParseFeed(context: Context) : AsyncTask<Void, Void, Void>() {
    private val TAG: String = "AsyncTask"

    private val contexto: Context = context
    private val activity: Activity = context as Activity
    private val listView: ListView = activity.findViewById<ListView>(R.id.listView)

    private val modelList = ArrayList<Model>()
    private val toBeParsed: Int = 15
    private val progressDialog: ProgressBar = activity.findViewById(R.id.progressBar)
    private val adapter: CustomAdapter = CustomAdapter(contexto, modelList)

    override fun doInBackground(vararg params: Void?): Void? {
        val rssLink = "http://istitutobrunofranchetti.gov.it/giornalino/feed/"
        val itemList = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(BufferedInputStream(URL(rssLink).openConnection().inputStream)).getElementsByTagName("item")

        activity.runOnUiThread { listView.adapter = adapter }

        for (i in 0 until toBeParsed) {
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
            activity.runOnUiThread {
                modelList.add(model)
                adapter.notifyDataSetChanged()
            }

            progressDialog.progress = i
        }
        Log.d(TAG, "Parsed all the ${modelList.size} articles correctly.")
        return null
    }

    override fun onPreExecute() {
        super.onPreExecute()

        progressDialog.isIndeterminate = false
        progressDialog.max = (toBeParsed - 1)

        // TODO: create ProgressBar inflated in AppBar.
    }

    override fun onPostExecute(result: Void?) {
        super.onPostExecute(result)
        // progressDialog.dismiss()
    }
}
