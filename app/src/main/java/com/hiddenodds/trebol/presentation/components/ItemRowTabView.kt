package com.hiddenodds.trebol.presentation.components

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.hiddenodds.trebol.R

class ItemRowTabView: FrameLayout {
    constructor(context: Context): super(context)
    constructor(context: Context, attributeSet: AttributeSet):
            super(context, attributeSet)
    @BindView(R.id.tv_description)
    @JvmField var tvDescription: TextView? = null
    @BindView(R.id.et_value)
    @JvmField var etValue: EditText? = null
    @OnClick(R.id.et_value)
    fun defineValue(){
        if (etValue!!.inputType == InputType.TYPE_NULL){
            if (etValue!!.text.isEmpty()){
                etValue!!.setText("V")
            }else{
                etValue!!.setText("")
            }
        }
    }

    init {
        LayoutInflater.from(context)
                .inflate(R.layout.view_item_tab, this, true)
        ButterKnife.bind(this)

    }
}