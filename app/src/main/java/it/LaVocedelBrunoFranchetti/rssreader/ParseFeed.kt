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
        val rsslink = "http://istitutobrunofranchetti.gov.it/giornalino/feed/"
        val url = URL(rsslink)
        val connection = url.openConnection() as HttpURLConnection
        val bufferedInputStream = BufferedInputStream(connection.inputStream)
        val documentBuilderFactory = DocumentBuilderFactory.newInstance()
        val documentBuilder = documentBuilderFactory.newDocumentBuilder()
        val document = documentBuilder.parse(bufferedInputStream)
        val itemlist = document.getElementsByTagName("item")

        for (i in 0 until itemlist.length) {
            val element = itemlist.item(i) as Element

            val nodeListTitle = element.getElementsByTagName("title")
            val nodeListLink = element.getElementsByTagName("link")
            val nodeListDate = element.getElementsByTagName("pubDate")
            val nodeListCreator = element.getElementsByTagName("dc:creator")
            val nodeListDescription = element.getElementsByTagName("description")
            val nodeListComment = element.getElementsByTagName("slash:comments")
            val nodeListCategory = element.getElementsByTagName("category")

            val title = nodeListTitle.item(i).firstChild.nodeValue
            val link = nodeListLink.item(i).firstChild.nodeValue
            val date = nodeListDate.item(i).firstChild.nodeValue
            val creator = nodeListCreator.item(i).firstChild.nodeValue
            val description = nodeListDescription.item(i).firstChild.nodeValue
            val comment = nodeListComment.item(i).firstChild.nodeValue
            // TODO: add interaction with category.
            val category = nodeListCategory.item(i).firstChild.nodeValue

            println(title)
            dataParsed!![i][1] = title
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
        println(dataParsed!![5][6])
    }
}
