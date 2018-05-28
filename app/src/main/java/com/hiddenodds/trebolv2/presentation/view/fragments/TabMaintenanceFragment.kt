package com.hiddenodds.trebolv2.presentation.view.fragments

import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import com.hiddenodds.trebolv2.R
import com.hiddenodds.trebolv2.presentation.components.ItemTabAdapter
import com.hiddenodds.trebolv2.presentation.model.GuideModel
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.experimental.async


class TabMaintenanceFragment: TabBaseFragment(){
    private val sufix = "_t1"
    private var adapter: ItemTabAdapter? = null
    private var flagChange = false
    @BindView(R.id.sv_tab)
    @JvmField var svTab: NestedScrollView? = null
    @BindView(R.id.rv_verification)
    @JvmField var rvVerification: RecyclerView? = null

    init {

        val message = observableMessageLoad.map { s -> s }
        disposable.add(message.observeOn(AndroidSchedulers.mainThread())
                .subscribe { s ->
                    kotlin.run {
                        if (s == YES){
                            setDataToControl(adapter!!, rvVerification!!)

                            println("SETDATACONTROL Reactive Maintenance")
                        }
                    }
                })
    }

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater!!.inflate(R.layout.view_tab_verification,
                container,false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view!!)
        setupRecyclerView(rvVerification!!)
        setAdapter()
    }

    fun setTableToBitmap(){
        if (rvVerification != null) rvVerification!!.clearFocus()
        if (isCreateImage(rvVerification, flagChange, sufix)){

            async {
                saveTableToBitmap(sufix, rvVerification!!)
                pdfGuideModel.nameMaintenance = sufix
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
                maintenanceModel!!::maintenance1.name -> {
                    if (maintenanceModel!!.maintenance1 != value){
                        flag = true
                        maintenanceModel!!.maintenance1 = value
                    }

                }
                maintenanceModel!!::maintenance2.name -> {
                    if (maintenanceModel!!.maintenance2 != value){
                        flag = true
                        maintenanceModel!!.maintenance2 = value
                    }
                }
                maintenanceModel!!::maintenance3.name -> {
                    if (maintenanceModel!!.maintenance3 != value){
                        flag = true
                        maintenanceModel!!.maintenance3 = value
                    }
                }
                maintenanceModel!!::maintenance4.name -> {
                    if (maintenanceModel!!.maintenance4 != value){
                        flag = true
                        maintenanceModel!!.maintenance4 = value
                    }
                }
                maintenanceModel!!::maintenance5.name -> {
                    if (maintenanceModel!!.maintenance5 != value){
                        flag = true
                        maintenanceModel!!.maintenance5 = value
                    }
                }
                maintenanceModel!!::maintenance6.name -> {
                    if (maintenanceModel!!.maintenance6 != value){
                        flag = true
                        maintenanceModel!!.maintenance6 = value
                    }
                }
                maintenanceModel!!::maintenance7.name -> {
                    if (maintenanceModel!!.maintenance7 != value){
                        flag = true
                        maintenanceModel!!.maintenance7 = value
                    }
                }
                maintenanceModel!!::maintenance8.name -> {
                    if (maintenanceModel!!.maintenance8 != value){
                        flag = true
                        maintenanceModel!!.maintenance8 = value
                    }
                }
                maintenanceModel!!::maintenance9.name -> {
                    if (maintenanceModel!!.maintenance9 != value){
                        flag = true
                        maintenanceModel!!.maintenance9 = value
                    }
                }
                maintenanceModel!!::maintenance10.name -> {
                    if (maintenanceModel!!.maintenance10 != value){
                        flag = true
                        maintenanceModel!!.maintenance10 = value
                    }
                }
                maintenanceModel!!::maintenance11.name -> {
                    if (maintenanceModel!!.maintenance11 != value){
                        flag = true
                        maintenanceModel!!.maintenance11 = value
                    }
                }
                maintenanceModel!!::maintenance12.name -> {
                    if (maintenanceModel!!.maintenance12 != value){
                        flag = true
                        maintenanceModel!!.maintenance12 = value
                    }
                }
                maintenanceModel!!::maintenance13.name -> {
                    if (maintenanceModel!!.maintenance13 != value){
                        flag = true
                        maintenanceModel!!.maintenance13 = value
                    }
                }
                maintenanceModel!!::maintenance14.name -> {
                    if (maintenanceModel!!.maintenance14 != value){
                        flag = true
                        maintenanceModel!!.maintenance14 = value
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
        val lbl = "maintenance"
        val free = listOf(0, 1, 8, 10, 11, 12, 13)

        val items: ArrayList<GuideModel> = ArrayList()
        val labels = context
                .resources.getStringArray(R.array.lbl_maintenance)
        val values: ArrayList<String> = ArrayList()
        values.add(maintenanceModel!!.maintenance1)
        values.add(maintenanceModel!!.maintenance2)
        values.add(maintenanceModel!!.maintenance3)
        values.add(maintenanceModel!!.maintenance4)
        values.add(maintenanceModel!!.maintenance5)
        values.add(maintenanceModel!!.maintenance6)
        values.add(maintenanceModel!!.maintenance7)
        values.add(maintenanceModel!!.maintenance8)
        values.add(maintenanceModel!!.maintenance9)
        values.add(maintenanceModel!!.maintenance10)
        values.add(maintenanceModel!!.maintenance11)
        values.add(maintenanceModel!!.maintenance12)
        values.add(maintenanceModel!!.maintenance13)
        values.add(maintenanceModel!!.maintenance14)

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