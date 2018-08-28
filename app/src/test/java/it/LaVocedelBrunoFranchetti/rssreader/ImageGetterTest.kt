package it.LaVocedelBrunoFranchetti.rssreader

import android.graphics.drawable.BitmapDrawable
import org.junit.Assert.assertTrue
import org.junit.Test

class ImageGetterTest {
    @Test
    fun imageGetterTest() {
        assertTrue(ImageGetter().getDrawable("https://raw.githubusercontent.com/franchetti/rssparser-android/kotlin/app/src/main/res/mipmap-xxxhdpi/ic_launcher.png") is BitmapDrawable)
    }
}