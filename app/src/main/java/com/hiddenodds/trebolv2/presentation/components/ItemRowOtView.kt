package com.hiddenodds.trebolv2.presentation.components

import android.content.Context
import android.support.v4.app.FragmentActivity
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.hiddenodds.trebolv2.R
import com.hiddenodds.trebolv2.presentation.view.fragments.OrderFragment

class ItemRowOtView: FrameLayout {
    constructor(context: Context): super(context)
    constructor(context: Context, attributeSet: AttributeSet):
            super(context, attributeSet)

    @BindView(R.id.edtAnnounce)
    @JvmField var edtAnnounce: TextView? = null
    @BindView(R.id.edtMachine)
    @JvmField var edtMachine: TextView? = null

    @BindView(R.id.edtType)
    @JvmField var edtType: TextView? = null
    @BindView(R.id.edtName)
    @JvmField var edtName: TextView? = null

    @BindView(R.id.edtContact)
    @JvmField var edtContact: TextView? = null
    @BindView(R.id.edtDate)
    @JvmField var edtDate: TextView? = null

    @BindView(R.id.edtDateFinish)
    @JvmField var edtDateFinish: TextView? = null
    @BindView(R.id.edtAddress)
    @JvmField var edtAddress: TextView? = null

    @BindView(R.id.edtProvince)
    @JvmField var edtProvince: TextView? = null
    @BindView(R.id.edtLocation)
    @JvmField var edtLocation: TextView? = null

    @BindView(R.id.edtPhone)
    @JvmField var edtPhone: TextView? = null
    @BindView(R.id.edtObservation)
    @JvmField var edtObservation: TextView? = null

    @BindView(R.id.btnOpenOt)
    @JvmField var btnOpenOt: Button? = null
    @BindView(R.id.btnEmail)
    @JvmField var btnEmail: Button? = null
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
    /*@BindView(R.id.view_space)
    @JvmField var viewSpace: View? = null*/
    init {
        LayoutInflater.from(context)
                .inflate(R.layout.view_item_ot, this, true)
        ButterKnife.bind(this)

    }

}