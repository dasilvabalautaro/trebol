package com.hiddenodds.trebolv2.presentation.view.fragments

import android.os.Build
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import com.hiddenodds.trebolv2.R
import com.hiddenodds.trebolv2.presentation.components.ItemTabAdapter
import com.hiddenodds.trebolv2.presentation.model.GuideModel
import com.hiddenodds.trebolv2.tools.ChangeFormat
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.experimental.async


class TabVerificationFragment: TabBaseFragment() {
    @BindView(R.id.rv_verification)
    @JvmField var rvVerification: RecyclerView? = null

    private var adapter: ItemTabAdapter? = null

    init {
        val message = observableMessageLoad.map { s -> s }
        disposable.add(message.observeOn(AndroidSchedulers.mainThread())
                .subscribe { s ->
                    kotlin.run {
                        if (s == YES){
                            setDataToControl()
                            println("SETDATACONTROL Reactive")
                        }
                    }
                })
    }

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root: View = inflater!!.inflate(R.layout.view_tab_verification,
                container,false)
        ButterKnife.bind(this, root)
        return root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        executeGetMaintenance()
        setupRecyclerView()
    }

 /*   override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

    }*/

    private fun setDataToControl(){

        async {
            val list = buildListOfData()
            adapter!!.setObjectList(list)
            activity.runOnUiThread({
                rvVerification!!.scrollToPosition(0)
            })
        }


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

    private fun sendUpdate(nameField: String, value: String){
        async {
            updateFieldMaintenancePresenter
                    .updateMaintenance(maintenanceModel!!.id,
                            nameField,
                            value)
        }
    }
    private fun updateField(nameField: String, value: String){
        if (maintenanceModel != null){
            var flag = false

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

            if (flag) sendUpdate(nameField, value)

        }
    }
    
    private fun buildListOfData(): ArrayList<GuideModel>{
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