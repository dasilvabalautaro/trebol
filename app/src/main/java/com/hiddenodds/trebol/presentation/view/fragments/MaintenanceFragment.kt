package com.hiddenodds.trebol.presentation.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.tabs.TabLayout
import com.hiddenodds.trebol.presentation.extension.setupWithViewPager
import com.hiddenodds.trebol.R
import com.hiddenodds.trebol.presentation.components.MaintenancePagesAdapter
import com.hiddenodds.trebol.presentation.model.GuideModel
import com.hiddenodds.trebol.tools.ChangeFormat
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class MaintenanceFragment: TabBaseFragment() {
    override fun buildListOfData(): ArrayList<GuideModel> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun buildListOfData(arrayTemp: java.util.ArrayList<String>): java.util.ArrayList<GuideModel> {
        TODO("Not yet implemented")
    }

    override fun updateField(nameField: String, value: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

    }

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tl_options)
    @JvmField var tlOptions: TabLayout? = null
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.vp_list)
    @JvmField var vpList: ViewPager2? = null

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

    private val codeNotification: String? by lazy { this.requireArguments().getString(inputNotification) }
    private val codeTechnical: String? by lazy { this.requireArguments().getString(inputTechnical) }
    private var beforePosition = -1
    private var positionUpdate = 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val root: View = inflater.inflate(R.layout.view_maintenance,
                container,false)
        ButterKnife.bind(this, root)

        codeNotify = codeNotification
        codeTech = codeTechnical
        beforePosition = 0
        vpList!!.adapter = MaintenancePagesAdapter(
                requireActivity().supportFragmentManager, lifecycle)

        vpList!!.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){

            override fun onPageSelected(position: Int) {
                positionUpdate = position
                when(beforePosition){
                    0 -> {
                        val fragmentVerification = (vpList!!.adapter as MaintenancePagesAdapter)
                                .createFragment(0)
                        (fragmentVerification as TabVerificationFragment).setTableToBitmap()

                    }
                    1 -> {

                        val fragmentMaintenance = (vpList!!.adapter as MaintenancePagesAdapter)
                                .createFragment(1)
                        (fragmentMaintenance as TabMaintenanceFragment).setTableToBitmap()

                    }
                    2 -> {
                        val fragmentTest = (vpList!!.adapter as MaintenancePagesAdapter)
                                .createFragment(2)
                        (fragmentTest as TabTestFragment).setTableToBitmap()

                    }
                    3 -> {
                        val fragmentObservation = (vpList!!.adapter as MaintenancePagesAdapter)
                                .createFragment(3)
                        (fragmentObservation as TabObservationFragment).setTableToBitmap()

                    }
                    else -> {
                        println("Position: $beforePosition")
                    }
                }

                beforePosition = position
            }

        })

        tlOptions!!.setupWithViewPager(vpList!!, listOf(requireContext().getString(R.string.lbl_tab_verify),
                requireContext().getString(R.string.lbl_tab_maintenance),
                requireContext().getString(R.string.lbl_tab_test),
                requireContext().getString(R.string.lbl_tab_observations)))

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (outState != null){
            codeNotify = outState!!.getString("codeNotify")
            codeTech = outState!!.getString("codeTech")

        }
        /*tlOptions!!.getTabAt(0)!!.text = requireContext().getString(R.string.lbl_tab_verify)
        tlOptions!!.getTabAt(1)!!.text = requireContext().getString(R.string.lbl_tab_maintenance)
        tlOptions!!.getTabAt(2)!!.text = requireContext().getString(R.string.lbl_tab_test)
        tlOptions!!.getTabAt(3)!!.text = requireContext().getString(R.string.lbl_tab_observations)
*/
    }

    override fun onResume() {
        super.onResume()
        GlobalScope.async {
            removeFragment()
        }

        vpList!!.postDelayed({ vpList!!.setCurrentItem(0,
                true) }, 100)

    }

    private fun removeFragment(){
        try {
            val manager = requireActivity().supportFragmentManager

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

    }

    override fun onDestroy() {
        super.onDestroy()
        codeNotification?.let { ChangeFormat.deleteCache(it) }
        codeNotify = null
        codeTech = null
        outState = null
    }
}

