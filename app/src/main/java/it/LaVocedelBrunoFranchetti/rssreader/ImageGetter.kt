package it.LaVocedelBrunoFranchetti.rssreader

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.Html
import java.io.IOException
import java.net.URL

class ImageGetter : Html.ImageGetter {
    override fun getDrawable(source: String): Drawable? {
        val bmp: BitmapDrawable
        return try {
            bmp = BitmapDrawable(BitmapFactory.decodeStream(URL(source).openConnection().getInputStream()))
            bmp.setBounds(0, 0, bmp.intrinsicWidth, bmp.intrinsicHeight)
            bmp
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}