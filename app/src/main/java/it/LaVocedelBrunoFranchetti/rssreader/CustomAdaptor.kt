package it.LaVocedelBrunoFranchetti.rssreader

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.StrictMode
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat.startActivity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

import java.text.SimpleDateFormat
import java.util.*



class CustomAdaptor internal constructor(private val context: Context, private val modelList: ArrayList<Model>) : BaseAdapter() {

    private var policy: StrictMode.ThreadPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build()
    private val holder = ViewHolder()
    private val rootView = (context as Activity).layoutInflater.inflate(R.layout.custom_list, null) as ConstraintLayout
    private val TAG = "CustomAdaptor"

    init {
        holder.dateandcreator = rootView.findViewById(R.id.date_and_creator) as TextView
        holder.dateandcreator = rootView.findViewById(R.id.date_and_creator) as TextView
        holder.dateandcreator = rootView.findViewById(R.id.date_and_creator) as TextView
        rootView.tag = holder
    }

    override fun getCount(): Int {
        return modelList.size
    }

    override fun getItem(i: Int): Any {
        return modelList[i]
    }

    override fun getItemId(i: Int): Long {
        return 0
    }

    override fun getView(i: Int, view: View, viewGroup: ViewGroup): View {
        StrictMode.setThreadPolicy(policy)


        val model = modelList[i]

        val title = model.title
        val date = model.date
        val creator = model.creator
        val description = model.description

        val dateFormat = SimpleDateFormat("%02d:%02d | dd.MM.yyyy", Locale.getDefault())
        val dateandcreator: String = dateFormat.parse(date).toString() + "   |   " + creator

        holder.title!!.text = model.title
        holder.dateandcreator!!.text = dateandcreator
        holder.description!!.text = description.toString()

        rootView.setOnClickListener {
            val link = model.link
            val intent = Intent(context, ArticleView::class.java)
            intent.putExtra("link", link)
            intent.putExtra("title", title)
            intent.putExtra("creator", creator)
            context.startActivity(intent)
        }

        rootView.setOnTouchListener { view, motionEvent -> false }*/

        Log.d(TAG, dateandcreator)
        return rootView
    }
}