package it.LaVocedelBrunoFranchetti.rssreader

import android.os.AsyncTask
import android.util.Log
import org.w3c.dom.Element

import java.io.BufferedInputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

import javax.xml.parsers.DocumentBuilderFactory

class ParseFeed : AsyncTask<Void, Void, String>() {
    override fun doInBackground(vararg params: Void?): String? {
        val url = URL("http://istitutobrunofranchetti.gov.it/giornalino/feed")
        val connection = url.openConnection() as HttpURLConnection
        val bufferedInputStream = BufferedInputStream(connection.inputStream)
        val documentBuilderFactory = DocumentBuilderFactory.newInstance()
        val documentBuilder = documentBuilderFactory.newDocumentBuilder()
        val document = documentBuilder.parse(bufferedInputStream)
        val itemlist = document.getElementsByTagName("item")
        lateinit var dataParsed: Array<Array<String>>

        for (i in 0 until itemlist.length) {
            val element = itemlist.item(i) as Element

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
            val comment = nodeListComment.item(0).firstChild.nodeValue
            // TODO: add interaction with category.
            val category = nodeListCategory.item(0).firstChild.nodeValue

            dataParsed[i][1] = title
            dataParsed[i][2] = link
            dataParsed[i][3] = date
            dataParsed[i][4] = creator
            dataParsed[i][5] = description
            dataParsed[i][6] = comment
        }
        return Arrays.toString(dataParsed)
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
