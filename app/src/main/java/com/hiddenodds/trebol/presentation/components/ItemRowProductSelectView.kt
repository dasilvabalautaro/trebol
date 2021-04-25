package com.hiddenodds.trebol.presentation.components

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.hiddenodds.trebol.R

class ItemRowProductSelectView: FrameLayout {
    constructor(context: Context): super(context)
    constructor(context: Context, attributeSet: AttributeSet):
            super(context, attributeSet)

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_quantity)
    @JvmField var etQuantity: EditText? = null
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_code)
    @JvmField var tvCode: TextView? = null
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ib_delete)
    @JvmField var ibDelete: ImageButton? = null
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_detail)
    @JvmField var tvDetail: TextView? = null

    init {
        LayoutInflater.from(context)
                .inflate(R.layout.view_item_product_select, this, true)
        ButterKnife.bind(this)

    }
}