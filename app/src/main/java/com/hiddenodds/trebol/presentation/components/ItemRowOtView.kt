package com.hiddenodds.trebol.presentation.components

import android.annotation.SuppressLint
import android.content.Context
import androidx.fragment.app.FragmentActivity
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.hiddenodds.trebol.R
import com.hiddenodds.trebol.presentation.view.fragments.MaintenanceFragment
import com.hiddenodds.trebol.presentation.view.fragments.OrderFragment
import com.hiddenodds.trebol.presentation.view.fragments.OtsFragment

class ItemRowOtView: FrameLayout {
    constructor(context: Context): super(context)
    constructor(context: Context, attributeSet: AttributeSet):
            super(context, attributeSet)

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.edtAnnounce)
    @JvmField var edtAnnounce: TextView? = null
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.edtMachine)
    @JvmField var edtMachine: TextView? = null

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.edtType)
    @JvmField var edtType: TextView? = null
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.edtName)
    @JvmField var edtName: TextView? = null

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.edtContact)
    @JvmField var edtContact: TextView? = null
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.edtDate)
    @JvmField var edtDate: TextView? = null

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.edtDateFinish)
    @JvmField var edtDateFinish: TextView? = null
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.edtAddress)
    @JvmField var edtAddress: TextView? = null

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.edtProvince)
    @JvmField var edtProvince: TextView? = null
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.edtLocation)
    @JvmField var edtLocation: TextView? = null

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.edtPhone)
    @JvmField var edtPhone: TextView? = null
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.edtObservation)
    @JvmField var edtObservation: TextView? = null

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btnGuide)
    @JvmField var btnGuide: Button? = null

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btnOpenOt)
    @JvmField var btnOpenOt: Button? = null
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btnEmail)
    @JvmField var btnEmail: Button? = null
    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btnOpenOt)
    fun openOT(){
        val params = (btnOpenOt!!.tag as String).split("*")
        if (params.isNotEmpty()){

            val editOtFragment = OrderFragment.newInstance(params[0],
                    params[1])
            (context as FragmentActivity).supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.flContent, editOtFragment,
                            editOtFragment.javaClass.simpleName)
                    .addToBackStack(null)
                    .commit()

        }

    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btnGuide)
    fun openGuide(){

        val params = (btnOpenOt!!.tag as String).split("*")
        if (params.isNotEmpty()){
            val maintenanceFragment = MaintenanceFragment.newInstance(params[0],
                    params[1])
            (context as FragmentActivity).supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.flContent, maintenanceFragment,
                            maintenanceFragment.javaClass.simpleName)
                    .addToBackStack(null)
                    .commit()
        }


    }

    init {
        LayoutInflater.from(context)
                .inflate(R.layout.view_item_ot, this, true)
        ButterKnife.bind(this)

    }

}