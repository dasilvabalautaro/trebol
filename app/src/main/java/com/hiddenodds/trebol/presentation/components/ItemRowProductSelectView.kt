package com.hiddenodds.trebol.presentation.components

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

    @BindView(R.id.et_quantity)
    @JvmField var etQuantity: EditText? = null
    @BindView(R.id.tv_code)
    @JvmField var tvCode: TextView? = null
    @BindView(R.id.ib_delete)
    @JvmField var ibDelete: ImageButton? = null
    @BindView(R.id.tv_detail)
    @JvmField var tvDetail: TextView? = null

    init {
        LayoutInflater.from(context)
                .inflate(R.layout.view_item_product_select, this, true)
        ButterKnife.bind(this)

    }
}