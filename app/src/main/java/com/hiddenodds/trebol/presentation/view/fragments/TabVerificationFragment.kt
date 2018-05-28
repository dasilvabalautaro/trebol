package com.hiddenodds.trebol.presentation.view.fragments

import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import com.hiddenodds.trebol.R
import com.hiddenodds.trebol.presentation.components.ItemTabAdapter
import com.hiddenodds.trebol.presentation.model.GuideModel
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async


class TabVerificationFragment: TabBaseFragment(){
    private val sufix = "_t0"
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

                            println("SETDATACONTROL Reactive")
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
        executeGetMaintenance()
    }

    fun setTableToBitmap(){
        if (rvVerification != null) rvVerification!!.clearFocus()

        if (isCreateImage(rvVerification, flagChange, sufix)){

            async(CommonPool) {
                saveTableToBitmap(sufix, rvVerification!!)
                pdfGuideModel.nameVerification = sufix
            }
            flagChange = false

        }
    }


    private fun setAdapter(){
        adapter = ItemTabAdapter{
            println("Value input: ${it.value}")
            flagChange = updateField(it.nameField, it.value)
        }
        rvVerification!!.adapter = adapter
    }

    override fun updateField(nameField: String, value: String): Boolean{
        var flag = false
        if (maintenanceModel != null){
            when(nameField){
                maintenanceModel!!::verification1.name -> {
                    if (maintenanceModel!!.verification1 != value){
                        flag = true
                        maintenanceModel!!.verification1 = value
                    }

                }
                maintenanceModel!!::verification2.name -> {
                    if (maintenanceModel!!.verification2 != value){
                        flag = true
                        maintenanceModel!!.verification2 = value
                    }
                }
                maintenanceModel!!::verification3.name -> {
                    if (maintenanceModel!!.verification3 != value){
                        flag = true
                        maintenanceModel!!.verification3 = value
                    }
                }
                maintenanceModel!!::verification4.name -> {
                    if (maintenanceModel!!.verification4 != value){
                        flag = true
                        maintenanceModel!!.verification4 = value
                    }
                }
                maintenanceModel!!::verification5.name -> {
                    if (maintenanceModel!!.verification5 != value){
                        flag = true
                        maintenanceModel!!.verification5 = value
                    }
                }
                maintenanceModel!!::verification6.name -> {
                    if (maintenanceModel!!.verification6 != value){
                        flag = true
                        maintenanceModel!!.verification6 = value
                    }
                }
                maintenanceModel!!::verification7.name -> {
                    if (maintenanceModel!!.verification7 != value){
                        flag = true
                        maintenanceModel!!.verification7 = value
                    }
                }
                maintenanceModel!!::verification8.name -> {
                    if (maintenanceModel!!.verification8 != value){
                        flag = true
                        maintenanceModel!!.verification8 = value
                    }
                }
                maintenanceModel!!::verification9.name -> {
                    if (maintenanceModel!!.verification9 != value){
                        flag = true
                        maintenanceModel!!.verification9 = value
                    }
                }
                maintenanceModel!!::verification10.name -> {
                    if (maintenanceModel!!.verification10 != value){
                        flag = true
                        maintenanceModel!!.verification10 = value
                    }
                }
                maintenanceModel!!::verification11.name -> {
                    if (maintenanceModel!!.verification11 != value){
                        flag = true
                        maintenanceModel!!.verification11 = value
                    }
                }
                maintenanceModel!!::verification12.name -> {
                    if (maintenanceModel!!.verification12 != value){
                        flag = true
                        maintenanceModel!!.verification12 = value
                    }
                }
                maintenanceModel!!::verification13.name -> {
                    if (maintenanceModel!!.verification13 != value){
                        flag = true
                        maintenanceModel!!.verification13 = value
                    }
                }
                maintenanceModel!!::verification14.name -> {
                    if (maintenanceModel!!.verification14 != value){
                        flag = true
                        maintenanceModel!!.verification14 = value
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
        val lbl = "verification"
        val free = listOf(2, 3, 5)

        val items: ArrayList<GuideModel> = ArrayList()
        val labels = context
                .resources.getStringArray(R.array.lbl_verification)
        val values: ArrayList<String> = ArrayList()
        values.add(maintenanceModel!!.verification1)
        values.add(maintenanceModel!!.verification2)
        values.add(maintenanceModel!!.verification3)
        values.add(maintenanceModel!!.verification4)
        values.add(maintenanceModel!!.verification5)
        values.add(maintenanceModel!!.verification6)
        values.add(maintenanceModel!!.verification7)
        values.add(maintenanceModel!!.verification8)
        values.add(maintenanceModel!!.verification9)
        values.add(maintenanceModel!!.verification10)
        values.add(maintenanceModel!!.verification11)
        values.add(maintenanceModel!!.verification12)
        values.add(maintenanceModel!!.verification13)
        values.add(maintenanceModel!!.verification14)

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