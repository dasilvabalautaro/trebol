package com.hiddenodds.trebol.presentation.components

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import com.hiddenodds.trebol.presentation.view.fragments.TabMaintenanceFragment
import com.hiddenodds.trebol.presentation.view.fragments.TabObservationFragment
import com.hiddenodds.trebol.presentation.view.fragments.TabTestFragment
import com.hiddenodds.trebol.presentation.view.fragments.TabVerificationFragment


class MaintenancePagesAdapter(manager: FragmentManager,
                              pager: ViewPager):
        FragmentStatePagerAdapter(manager) {

    var maintenance: TabMaintenanceFragment? = null
    var observation: TabObservationFragment? = null
    var test: TabTestFragment? = null
    var verification: TabVerificationFragment? = null


    override fun getItem(position: Int): Fragment {
        when(position){
            0 ->{
                if (verification == null) verification = TabVerificationFragment()
                return verification!!

            }
            1 ->{
                if (maintenance == null) maintenance = TabMaintenanceFragment()
                return maintenance!!

            }
            2 ->{
                if (test == null) test = TabTestFragment()
                return test!!
            }
            3 ->{
                if (observation == null) observation = TabObservationFragment()
            }
        }
        return observation!!
    }

    override fun getCount(): Int {
        return 4
    }

}