package net.dvinfosys.circleprogress.App

import android.support.v4.app.Fragment
import android.widget.Button
import android.widget.FrameLayout
import android.os.Bundle
import android.R.id.button2
import android.R.id.button1
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import net.dvinfosys.circleprogress.R


class ArcInFragmentFragment : Fragment() {

    internal lateinit var fragmentContainer: FrameLayout
    internal lateinit var button1: Button
    internal lateinit var button2: Button
    internal lateinit var tab1: ArcTabFragment
    internal lateinit var tab2: ArcTabFragment

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_arc_in, container, false)
        fragmentContainer = view.findViewById(R.id.tab_container)
        if (savedInstanceState == null) {
            tab1 = ArcTabFragment()
            tab2 = ArcTabFragment()
            fragmentManager
                    .beginTransaction()
                    .add(R.id.tab_container, tab1, "tab1")
                    .add(R.id.tab_container, tab2, "tab2")
                    .hide(tab2)
                    .commit()
        } else {
            tab1 = fragmentManager.findFragmentByTag("tab1") as ArcTabFragment
            tab2 = fragmentManager.findFragmentByTag("tab2") as ArcTabFragment
            fragmentManager.beginTransaction().hide(tab2).commit()
        }
        button1 = view.findViewById(R.id.button1)
        button2 = view.findViewById(R.id.button2)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        button1.setOnClickListener(object : View.OnClickListener {
           override fun onClick(v: View) {
                fragmentManager
                        .beginTransaction()
                        .show(tab1)
                        .hide(tab2)
                        .commit()
                tab1.onSelect()
            }
        })

        button2.setOnClickListener(object : View.OnClickListener {
           override fun onClick(v: View) {
                fragmentManager
                        .beginTransaction()
                        .show(tab2)
                        .hide(tab1)
                        .commit()
                tab2.onSelect()
            }
        })
    }
}