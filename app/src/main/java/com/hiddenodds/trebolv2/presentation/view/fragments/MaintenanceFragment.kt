package com.hiddenodds.trebolv2.presentation.view.fragments

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import com.hiddenodds.trebolv2.R
import com.hiddenodds.trebolv2.presentation.components.MaintenancePagesAdapter
import com.hiddenodds.trebolv2.tools.ChangeFormat


class MaintenanceFragment: TabBaseFragment() {
    @BindView(R.id.tl_options)
    @JvmField var tlOptions: TabLayout? = null
    @BindView(R.id.vp_list)
    @JvmField var vpList: ViewPager? = null

    companion object Factory {
        private const val inputNotification = "notify_"
        private const val inputTechnical = "technical_"
        fun newInstance(arg1: String? = null, arg2: String? = null):
                MaintenanceFragment = MaintenanceFragment().apply{
            this.arguments = Bundle().apply {
                this.putString(inputNotification, arg1)
                this.putString(inputTechnical, arg2)
            }

        }
    }

    private val codeNotification: String by lazy { this.arguments.getString(inputNotification) }
    private val codeTechnical: String by lazy { this.arguments.getString(inputTechnical) }

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root: View = inflater!!.inflate(R.layout.view_maintenance,
                container,false)
        ButterKnife.bind(this, root)
        codeNotify = codeNotification
        vpList!!.adapter = MaintenancePagesAdapter(activity
                .supportFragmentManager, vpList!!)

        tlOptions!!.setupWithViewPager(vpList)
        return root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tlOptions!!.getTabAt(0)!!.text = context.getString(R.string.lbl_tab_verify)
        tlOptions!!.getTabAt(1)!!.text = context.getString(R.string.lbl_tab_maintenance)
        tlOptions!!.getTabAt(2)!!.text = context.getString(R.string.lbl_tab_test)
        tlOptions!!.getTabAt(3)!!.text = context.getString(R.string.lbl_tab_observations)
    }

    override fun onPause() {
        super.onPause()
        ChangeFormat.deleteCache(codeNotification)
        codeNotify = null
        maintenanceModel = null
    }

}