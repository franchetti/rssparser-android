package it.LaVocedelBrunoFranchetti.rssreader

import android.os.AsyncTask
import android.util.Log
import org.w3c.dom.Element

import java.io.BufferedInputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

import javax.xml.parsers.DocumentBuilderFactory

class ParseFeed : AsyncTask<Void, Void, ArrayList<Model?>>() {
    private val TAG: String = "AsyncTask"
    private val modelList = ArrayList<Model?>()

    override fun doInBackground(vararg params: Void?): ArrayList<Model?> {
        val rssLink = "http://istitutobrunofranchetti.gov.it/giornalino/feed/"
        val url = URL(rssLink)
        val connection = url.openConnection() as HttpURLConnection
        val bufferedInputStream = BufferedInputStream(connection.inputStream)
        val documentBuilderFactory = DocumentBuilderFactory.newInstance()
        val documentBuilder = documentBuilderFactory.newDocumentBuilder()
        val document = documentBuilder.parse(bufferedInputStream)
        val itemList = document.getElementsByTagName("item")

        for (i in 0 until itemList.length) {
            val element = itemList.item(i) as Element

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
            val comment = nodeListComment.item(i).firstChild.nodeValue
            val category = nodeListCategory.item(i).firstChild.nodeValue */

            val model: Model? = null
            model?.title = title
            model?.link = link
            model?.date = date
            model?.creator = creator
            model?.description = description
            modelList.add(model)
        }
        Log.d(TAG, "Parsed all the articles correctly.")
        // TODO: Work here.
        return modelList
    }

    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun onPostExecute(result: ArrayList<Model?>) {
        super.onPostExecute(result)
        
    }

    override fun onProgressUpdate(vararg values: Void?) {
        super.onProgressUpdate(*values)
    }
}
