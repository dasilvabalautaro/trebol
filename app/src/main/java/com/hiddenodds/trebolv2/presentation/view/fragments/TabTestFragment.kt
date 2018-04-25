package com.hiddenodds.trebolv2.presentation.view.fragments

import android.os.Build
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import com.hiddenodds.trebolv2.R
import com.hiddenodds.trebolv2.presentation.components.ItemTabAdapter
import com.hiddenodds.trebolv2.presentation.model.GuideModel
import com.hiddenodds.trebolv2.tools.ChangeFormat
import kotlinx.coroutines.experimental.async


class TabTestFragment: TabBaseFragment(){

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater!!.inflate(R.layout.view_tab_verification,
                container,false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view!!)
        setupRecyclerView()
    }

    override fun onStart() {
        super.onStart()
        setDataToControl()
    }

    private fun setupRecyclerView(){
        rvVerification!!.setHasFixedSize(true)
        rvVerification!!.layoutManager = LinearLayoutManager(activity,
                LinearLayoutManager.VERTICAL, false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ChangeFormat.addDecorationRecycler(rvVerification!!, context)
        }
        adapter = ItemTabAdapter{
            updateField(it.nameField, it.value)
        }

        rvVerification!!.adapter = adapter
    }

    private fun setDataToControl(){

        async {
            val list = buildListOfData()
            adapter!!.setObjectList(list)
            activity.runOnUiThread({
                rvVerification!!.scrollToPosition(0)
            })
        }


    }

    private fun updateField(nameField: String, value: String){
        if (maintenanceModel != null){
            var flag = false

            when(nameField){
                maintenanceModel!!::test1.name -> {
                    if (maintenanceModel!!.test1 != value){
                        flag = true
                        maintenanceModel!!.test1 = value
                    }

                }
                maintenanceModel!!::test2.name -> {
                    if (maintenanceModel!!.test2 != value){
                        flag = true
                        maintenanceModel!!.test2 = value
                    }
                }
                maintenanceModel!!::test3.name -> {
                    if (maintenanceModel!!.test3 != value){
                        flag = true
                        maintenanceModel!!.test3 = value
                    }
                }
                maintenanceModel!!::test4.name -> {
                    if (maintenanceModel!!.test4 != value){
                        flag = true
                        maintenanceModel!!.test4 = value
                    }
                }
                maintenanceModel!!::test5.name -> {
                    if (maintenanceModel!!.test5 != value){
                        flag = true
                        maintenanceModel!!.test5 = value
                    }
                }
                maintenanceModel!!::test6.name -> {
                    if (maintenanceModel!!.test6 != value){
                        flag = true
                        maintenanceModel!!.test6 = value
                    }
                }
                maintenanceModel!!::test7.name -> {
                    if (maintenanceModel!!.test7 != value){
                        flag = true
                        maintenanceModel!!.test7 = value
                    }
                }
                maintenanceModel!!::test8.name -> {
                    if (maintenanceModel!!.test8 != value){
                        flag = true
                        maintenanceModel!!.test8 = value
                    }
                }
                maintenanceModel!!::test9.name -> {
                    if (maintenanceModel!!.test9 != value){
                        flag = true
                        maintenanceModel!!.test9 = value
                    }
                }
                maintenanceModel!!::test10.name -> {
                    if (maintenanceModel!!.test10 != value){
                        flag = true
                        maintenanceModel!!.test10 = value
                    }
                }
                maintenanceModel!!::test11.name -> {
                    if (maintenanceModel!!.test11 != value){
                        flag = true
                        maintenanceModel!!.test11 = value
                    }
                }
                maintenanceModel!!::test12.name -> {
                    if (maintenanceModel!!.test12 != value){
                        flag = true
                        maintenanceModel!!.test12 = value
                    }
                }
                maintenanceModel!!::test13.name -> {
                    if (maintenanceModel!!.test13 != value){
                        flag = true
                        maintenanceModel!!.test13 = value
                    }
                }
                maintenanceModel!!::test14.name -> {
                    if (maintenanceModel!!.test14 != value){
                        flag = true
                        maintenanceModel!!.test14 = value
                    }
                }
                maintenanceModel!!::test15.name -> {
                    if (maintenanceModel!!.test15 != value){
                        flag = true
                        maintenanceModel!!.test15 = value
                    }
                }
                maintenanceModel!!::test16.name -> {
                    if (maintenanceModel!!.test16 != value){
                        flag = true
                        maintenanceModel!!.test16 = value
                    }
                }
            }

            if (flag) sendUpdate(nameField, value)

        }
    }

    private fun buildListOfData(): ArrayList<GuideModel>{
        val lbl = "test"
        val free = listOf(4, 5)

        val items: ArrayList<GuideModel> = ArrayList()
        val labels = context
                .resources.getStringArray(R.array.lbl_test)
        val values: ArrayList<String> = ArrayList()
        values.add(maintenanceModel!!.test1)
        values.add(maintenanceModel!!.test2)
        values.add(maintenanceModel!!.test3)
        values.add(maintenanceModel!!.test4)
        values.add(maintenanceModel!!.test5)
        values.add(maintenanceModel!!.test6)
        values.add(maintenanceModel!!.test7)
        values.add(maintenanceModel!!.test8)
        values.add(maintenanceModel!!.test9)
        values.add(maintenanceModel!!.test10)
        values.add(maintenanceModel!!.test11)
        values.add(maintenanceModel!!.test12)
        values.add(maintenanceModel!!.test13)
        values.add(maintenanceModel!!.test14)
        values.add(maintenanceModel!!.test15)
        values.add(maintenanceModel!!.test16)

        for (i in labels.indices){
            val guideModel = GuideModel()
            guideModel.description = labels[i]
            guideModel.value = values[i]
            val index = i + 1
            guideModel.nameField = "$lbl$index"
            val e = free.filter { it == i}
            if (e.isNotEmpty()){
                guideModel.free = 1
            }
            items.add(guideModel)
        }

        return items
    }
}