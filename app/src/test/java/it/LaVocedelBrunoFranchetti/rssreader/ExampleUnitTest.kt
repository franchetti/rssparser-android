package it.LaVocedelBrunoFranchetti.rssreader

import android.app.Activity
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    // TODO: Setup a working test.
    @Test
    fun rss_parseCorrectly() {
        val rssLink = "http://istitutobrunofranchetti.gov.it/giornalino/feed/"
        ParseFeed()
        assertEquals("Giorgio", "")
    }
}
