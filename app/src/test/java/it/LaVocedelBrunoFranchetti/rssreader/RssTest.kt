package it.LaVocedelBrunoFranchetti.rssreader

import android.app.Activity
import android.widget.TextView
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class RssTest: Activity() {
    // TODO: Setup a working test.
    @Test
    fun rss_parseCorrectly() {
        ParseFeed(this)
        val firstAuthor = findViewById<TextView>(R.id.title).text
        assertEquals("Giorgio", firstAuthor)
    }
}
