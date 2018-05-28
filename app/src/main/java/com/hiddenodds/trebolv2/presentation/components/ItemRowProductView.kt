package com.hiddenodds.trebolv2.presentation.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.hiddenodds.trebolv2.R

class ItemRowProductView: FrameLayout {
    constructor(context: Context): super(context)
    constructor(context: Context, attributeSet: AttributeSet):
            super(context, attributeSet)
    @BindView(R.id.tv_code)
    @JvmField var tvCode: TextView? = null
    @BindView(R.id.tv_description)
    @JvmField var tvDescription: TextView? = null

    init {
        LayoutInflater.from(context)
                .inflate(R.layout.view_item_product, this, true)
        ButterKnife.bind(this)

    }
}