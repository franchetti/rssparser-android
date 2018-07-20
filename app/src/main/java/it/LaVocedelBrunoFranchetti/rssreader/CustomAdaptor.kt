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
import java.util.Date

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
        val date_and_creator = rootView.findViewById<View>(R.id.date_and_creator) as TextView
        val title = rootView.findViewById<View>(R.id.title) as TextView
        //        final ImageView image = (ImageView) rootView.findViewById(R.id.resim);
        //        final TextView description = (TextView) rootView.findViewById(R.id.description);
        val model = modelList[i]

        val date = Date(model.date)

        val dateFormat = SimpleDateFormat("dd.MM.yyyy")
        title.text = model.title
        date_and_creator.text = String.format("%02d:%02d", date.hours, date.minutes) + " | " +
                dateFormat.format(date) + "   |   " +
                model.creator
        /*        Bitmap bitmap = model.getImage();
        image.setImageBitmap(bitmap);
        description.setText(description.toString());
*/
        rootView.setOnClickListener {
            val link = model.link
            val title = model.title
            val creator = model.creator
            val intent = Intent(context, webb::class.java)
            intent.putExtra("link", link)
            intent.putExtra("title", title)
            intent.putExtra("creator", creator)
            context.startActivity(intent)
        }

        rootView.setOnTouchListener { view, motionEvent -> false }

        return rootView
    }
}