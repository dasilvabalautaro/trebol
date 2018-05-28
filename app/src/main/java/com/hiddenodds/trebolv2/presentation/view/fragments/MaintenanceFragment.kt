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
import com.hiddenodds.trebolv2.presentation.model.GuideModel
import com.hiddenodds.trebolv2.tools.ChangeFormat
import kotlinx.coroutines.experimental.async


class MaintenanceFragment: TabBaseFragment() {
    override fun buildListOfData(): ArrayList<GuideModel> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateField(nameField: String, value: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

    }

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
    private var beforePosition = -1
    private var positionUpdate = 0


    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root: View = inflater!!.inflate(R.layout.view_maintenance,
                container,false)
        ButterKnife.bind(this, root)

        codeNotify = codeNotification
        codeTech = codeTechnical
        beforePosition = 0
        vpList!!.adapter = MaintenancePagesAdapter(activity
                .supportFragmentManager, vpList!!)
        vpList!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }
            override fun onPageSelected(position: Int) {
                positionUpdate = position
                when(beforePosition){
                    0 -> {
                        val fragmentVerification = (vpList!!.adapter as MaintenancePagesAdapter)
                                .getItem(0)
                        (fragmentVerification as TabVerificationFragment).setTableToBitmap()

                    }
                    1 -> {

                        val fragmentMaintenance = (vpList!!.adapter as MaintenancePagesAdapter)
                                .getItem(1)
                        (fragmentMaintenance as TabMaintenanceFragment).setTableToBitmap()

                    }
                    2 -> {
                        val fragmentTest = (vpList!!.adapter as MaintenancePagesAdapter)
                                .getItem(2)
                        (fragmentTest as TabTestFragment).setTableToBitmap()

                    }
                    3 -> {
                        val fragmentObservation = (vpList!!.adapter as MaintenancePagesAdapter)
                                .getItem(3)
                        (fragmentObservation as TabObservationFragment).setTableToBitmap()

                    }
                    else -> {
                        println("Position: $beforePosition")
                    }
                }

                beforePosition = position
            }

        })

        tlOptions!!.setupWithViewPager(vpList)
        return root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (outState != null){
            codeNotify = outState!!.getString("codeNotify")
            codeTech = outState!!.getString("codeTech")
            /*maintenanceModel = outState!!
                    .getSerializable("maintenanceModel") as MaintenanceModel
            positionUpdate = outState!!.getInt("position")
            beforePosition = outState!!.getInt("positionBefore")*/
        }

        tlOptions!!.getTabAt(0)!!.text = context.getString(R.string.lbl_tab_verify)
        tlOptions!!.getTabAt(1)!!.text = context.getString(R.string.lbl_tab_maintenance)
        tlOptions!!.getTabAt(2)!!.text = context.getString(R.string.lbl_tab_test)
        tlOptions!!.getTabAt(3)!!.text = context.getString(R.string.lbl_tab_observations)

    }

    override fun onResume() {
        super.onResume()
        async {
            removeFragment()
        }

        vpList!!.postDelayed({ vpList!!.setCurrentItem(0,
                true) }, 100)

    }

    private fun removeFragment(){
        try {
            val manager = activity.supportFragmentManager

            for (i in 0 until manager.backStackEntryCount){
                val fragment = manager.fragments[i]
                if (fragment is EmailFragment){
                    manager.beginTransaction().remove(fragment).commit()
                    fragment.onDestroy()
                    break
                }

            }

        }catch(ex: Exception){
            println(ex.message)
        }
    }

    override fun onPause() {
        super.onPause()
        outState = Bundle()
        outState!!.putString("codeNotify", codeNotification)
        outState!!.putString("codeTech", codeTechnical)
        /*outState!!.putSerializable("maintenanceModel", maintenanceModel)
        outState!!.putInt("position", positionUpdate)
        outState!!.putInt("positionBefore", beforePosition)*/
    }

    override fun onDestroy() {
        super.onDestroy()
        ChangeFormat.deleteCache(codeNotification)
        codeNotify = null
        codeTech = null
        outState = null
    }
}