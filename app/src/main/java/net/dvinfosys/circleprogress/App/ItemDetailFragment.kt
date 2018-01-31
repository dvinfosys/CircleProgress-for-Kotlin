package net.dvinfosys.circleprogress.App

import android.widget.TextView
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.support.v4.app.Fragment
import android.view.View
import net.dvinfosys.circleprogress.App.dummy.DummyContent
import net.dvinfosys.circleprogress.R

class ItemDetailFragment : Fragment() {


    private var mItem: DummyContent.DummyItem? = null

   override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = DummyContent.ITEM_MAP[getArguments().getString(ARG_ITEM_ID)]
        }
    }

   override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                     savedInstanceState: Bundle?): View {
        val rootView = inflater?.inflate(R.layout.fragment_item_detail, container, false)

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            (rootView!!.findViewById<TextView>(R.id.item_detail) as TextView).text = mItem!!.content
        }

        return rootView!!
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        val ARG_ITEM_ID = "item_id"
    }
}