package it.LaVocedelBrunoFranchetti.rssreader

import android.widget.TextView

class Model {
    var title: String? = null
    var date: String? = null
    var creator: String? = null
    var link: String? = null
    var description: String? = null
}

internal class ViewHolder {
    var dateandcreator: TextView? = null
    var description: TextView? = null
    var title: TextView? = null
}