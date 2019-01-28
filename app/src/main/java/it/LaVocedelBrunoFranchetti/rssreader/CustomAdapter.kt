package it.LaVocedelBrunoFranchetti.rssreader

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.StrictMode
import androidx.constraintlayout.widget.ConstraintLayout
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*


class CustomAdapter internal constructor(private val context: Context, private val modelList: ArrayList<Model>) : BaseAdapter() {
    private var policy: StrictMode.ThreadPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build()
    private val TAG = "CustomAdaptor"

    override fun getCount(): Int {
        return modelList.size
    }

    override fun getItem(i: Int): Any {
        return modelList[i]
    }

    override fun getItemId(i: Int): Long {
        return getItem(i).hashCode().toLong()
    }

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View {
        StrictMode.setThreadPolicy(policy)
        val rootView: ConstraintLayout = (context as Activity).layoutInflater.inflate(R.layout.custom_list, null) as ConstraintLayout

        val date_and_creator: TextView = rootView.findViewById(R.id.date_and_creator) as TextView
        val title: TextView = rootView.findViewById(R.id.title) as TextView
        val description: TextView = rootView.findViewById(R.id.description) as TextView

        val model: Model = modelList[i]

        val creator: String? = model.creator
        // TODO: Remove usage of deprecated Java.Date, using Java.Time.
        val date = Date(model.date)
        val dateandcreator: String = (String.format("%02d:%02d", date.hours, date.minutes) + "   |   " + SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(date) + "   |   " + model.creator)

        date_and_creator.text = dateandcreator
        title.text = model.title
        description.text = model.description

        rootView.setOnClickListener {
            val link = model.link
            val intent = Intent(context, ArticleView::class.java)
            intent.putExtra("link", link)
            intent.putExtra("title", model.title)
            intent.putExtra("creator", creator)
            context.startActivity(intent)
        }

        // rootView.setOnTouchListener { view, motionEvent -> false }

        Log.d(TAG, model.title)
        return rootView
    }
}