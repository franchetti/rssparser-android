package it.LaVocedelBrunoFranchetti.rssreader

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.StrictMode
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView

import java.text.SimpleDateFormat
import java.util.*
import android.support.design.widget.CoordinatorLayout.Behavior.setTag



class CustomAdaptor internal constructor(private val context: Context, private val modelList: List<Model>) : BaseAdapter() {

    internal var policy: StrictMode.ThreadPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build()

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

        val rootView = (context as Activity).layoutInflater.inflate(R.layout.custom_list, null) as LinearLayout
        val holder = ViewHolder()
        holder.dateandcreator = convertView.findViewById(R.id.date_and_creator) as TextView
        convertView.setTag(holder)
        val dateandcreatorTW = rootView.findViewById(R.id.date_and_creator) as TextView
        val titleTW = rootView.findViewById<View>(R.id.title) as TextView
        val descriptionTW = rootView.findViewById<View>(R.id.description) as TextView
        val model = modelList[i]

        val date = model.date
        val creator = model.creator
        val description = model.description

        val dateFormat = SimpleDateFormat("%02d:%02d | dd.MM.yyyy", Locale.getDefault())
        val dateandcreator: String = dateFormat.parse(date).toString() + "   |   " + creator

        titleTW.text = model.title
        dateandcreatorTW.text = dateandcreator
        descriptionTW.text = description.toString()

        rootView.setOnClickListener {
            val link = model.link
            val title = model.title
            val creator = model.creator
            /* val intent = Intent(context, webb::class.java)
            intent.putExtra("link", link)
            intent.putExtra("title", title)
            intent.putExtra("creator", creator)
            context.startActivity(intent)*/
        }

        rootView.setOnTouchListener { view, motionEvent -> false }

        return rootView
    }
}