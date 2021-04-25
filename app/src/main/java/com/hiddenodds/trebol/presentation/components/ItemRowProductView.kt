package com.hiddenodds.trebol.presentation.components

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.hiddenodds.trebol.R

class ItemRowProductView: FrameLayout {
    constructor(context: Context): super(context)
    constructor(context: Context, attributeSet: AttributeSet):
            super(context, attributeSet)
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_code)
    @JvmField var tvCode: TextView? = null
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_description)
    @JvmField var tvDescription: TextView? = null

    init {
        LayoutInflater.from(context)
                .inflate(R.layout.view_item_product, this, true)
        ButterKnife.bind(this)

    }
}