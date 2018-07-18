package it.LaVocedelBrunoFranchetti.rssreader

import android.content.Context
import android.os.AsyncTask
import android.provider.Settings.System.getString
import android.util.Log
import it.LaVocedelBrunoFranchetti.rssreader.R.string.rss_link
import org.w3c.dom.Element

import java.io.BufferedInputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

import javax.xml.parsers.DocumentBuilderFactory

class ParseFeed : AsyncTask<Void, Void, String>() {

    override fun doInBackground(vararg params: Void?): String? {
        val rssLink = "http://istitutobrunofranchetti.gov.it/giornalino/feed/"
        val url = URL(rssLink)
        val connection = url.openConnection() as HttpURLConnection
        val bufferedInputStream = BufferedInputStream(connection.inputStream)
        val documentBuilderFactory = DocumentBuilderFactory.newInstance()
        val documentBuilder = documentBuilderFactory.newDocumentBuilder()
        val document = documentBuilder.parse(bufferedInputStream)
        val itemList = document.getElementsByTagName("item")
        val dataParsed = arrayOfNulls<String>(50)

        for (i in 0 until itemList.length) {
            val element = itemList.item(i) as Element

            val nodeListTitle = element.getElementsByTagName("title")
            val nodeListLink = element.getElementsByTagName("link")
            val nodeListDate = element.getElementsByTagName("pubDate")
            val nodeListCreator = element.getElementsByTagName("dc:creator")
            val nodeListDescription = element.getElementsByTagName("description")
            val nodeListComment = element.getElementsByTagName("slash:comments")
            val nodeListCategory = element.getElementsByTagName("category")

            val title = nodeListTitle.item(0).firstChild.nodeValue
            val link = nodeListLink.item(0).firstChild.nodeValue
            val date = nodeListDate.item(0).firstChild.nodeValue
            val creator = nodeListCreator.item(0).firstChild.nodeValue
            val description = nodeListDescription.item(0).firstChild.nodeValue
            // TODO: add interaction with category and comments.
            /* val comment = nodeListComment.item(i).firstChild.nodeValue
            val category = nodeListCategory.item(i).firstChild.nodeValue */

            Log.d("DEBUG","$title§$link§$date§$creator§$description")
            dataParsed[i] = "$title§$link§$date§$creator§$description"
        }
        // for (i in 0 until dataParsed.size) Log.d("DEBUG",dataParsed[i])
        return dataParsed[3]
    }

    override fun onPreExecute() {
        super.onPreExecute()
        // ...
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        // ...
    }
}
