package it.LaVocedelBrunoFranchetti.rssreader

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.StrictMode
import android.support.constraint.ConstraintLayout
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*



class CustomAdaptor internal constructor(private val context: Context, private val modelList: ArrayList<Model>) : BaseAdapter() {

    private var policy: StrictMode.ThreadPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build()
    private val rootView = (context as Activity).layoutInflater.inflate(R.layout.custom_list, null) as ConstraintLayout
    private val TAG = "CustomAdaptor"

    private val date_and_creatorTW = rootView.findViewById(R.id.date_and_creator) as TextView
    private val titleTW = rootView.findViewById(R.id.title) as TextView

    override fun getCount(): Int {
        return modelList.size
    }

    override fun getItem(i: Int): Any {
        return modelList[i]
    }

    override fun getItemId(i: Int): Long {
        return 0
    }

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View {
        StrictMode.setThreadPolicy(policy)

        val model = modelList[i]
        val title = model.title
        val creator = model.creator
        val description = model.description
        val date = Date(model.date)
        val dateandcreator: String = (String.format("%02d:%02d", date.hours, date.minutes) + "   |   " + SimpleDateFormat("%02d:%02d | dd.MM.yyyy", Locale.getDefault()).format(date) + "   |   " + model.creator)

        date_and_creatorTW.text = dateandcreator
        titleTW.text = title

        rootView.setOnClickListener {
            val link = model.link
            val intent = Intent(context, ArticleView::class.java)
            intent.putExtra("link", link)
            intent.putExtra("title", title)
            intent.putExtra("creator", creator)
            context.startActivity(intent)
        }

        // rootView.setOnTouchListener { view, motionEvent -> false }

        Log.d(TAG, dateandcreator)
        return rootView
    }
}