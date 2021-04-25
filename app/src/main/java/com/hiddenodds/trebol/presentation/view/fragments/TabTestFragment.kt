package com.hiddenodds.trebol.presentation.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.core.widget.NestedScrollView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.hiddenodds.trebol.R
import com.hiddenodds.trebol.presentation.components.ItemTabAdapter
import com.hiddenodds.trebol.presentation.model.GuideModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async


class TabTestFragment: TabBaseFragment(){
    private val sufix = "_t2"
    private var adapter: ItemTabAdapter? = null
    private var flagChange = false
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.sv_tab)
    @JvmField var svTab: NestedScrollView? = null
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rv_verification)
    @JvmField var rvVerification: RecyclerView? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.view_tab_verification,
                container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)
        setupRecyclerView(rvVerification!!)
        setAdapter()
        executeGetTechnical()
    }

    override fun onStart() {
        super.onStart()
        setDataToControl(adapter!!, rvVerification!!)
    }

    fun setTableToBitmap(){
        if (rvVerification != null) rvVerification!!.clearFocus()
        if (isCreateImage(rvVerification, flagChange, sufix)){

            GlobalScope.async {
                saveTableToBitmap(sufix, rvVerification!!)
                pdfGuideModel.nameTest = sufix
            }
            flagChange = false
        }
    }

    private fun setAdapter(){
        adapter = ItemTabAdapter{
            flagChange = updateField(it.nameField, it.value)

        }

        rvVerification!!.adapter = adapter
    }

    override fun updateField(nameField: String, value: String): Boolean{
        var flag = false
        if (maintenanceModel != null){

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

            if (flag){
                sendUpdate(nameField, value)
            }
        }

        return flag
    }

    override fun buildListOfData(): ArrayList<GuideModel>{
        val lbl = "test"
        val free = listOf(4, 5)

        val items: ArrayList<GuideModel> = ArrayList()
        val labels = requireContext()
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

    override fun buildListOfData(arrayTemp: java.util.ArrayList<String>): java.util.ArrayList<GuideModel> {
        TODO("Not yet implemented")
    }
}