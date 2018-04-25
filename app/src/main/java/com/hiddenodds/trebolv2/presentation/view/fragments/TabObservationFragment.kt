package com.hiddenodds.trebolv2.presentation.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import com.hiddenodds.trebolv2.R


class TabObservationFragment: TabBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root: View = inflater!!.inflate(R.layout.view_tab_observations,
                container,false)
        ButterKnife.bind(this, root)
        return root
    }

}