package com.hiddenodds.trebol.presentation.components

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hiddenodds.trebol.presentation.view.fragments.TabMaintenanceFragment
import com.hiddenodds.trebol.presentation.view.fragments.TabObservationFragment
import com.hiddenodds.trebol.presentation.view.fragments.TabTestFragment
import com.hiddenodds.trebol.presentation.view.fragments.TabVerificationFragment

class MaintenancePagesAdapter(manager: FragmentManager, lifecycle: Lifecycle):
        FragmentStateAdapter(manager, lifecycle) {

    var maintenance: TabMaintenanceFragment? = null
    var observation: TabObservationFragment? = null
    var test: TabTestFragment? = null
    var verification: TabVerificationFragment? = null

    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
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

}