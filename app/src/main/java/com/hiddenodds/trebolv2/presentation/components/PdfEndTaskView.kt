package com.hiddenodds.trebolv2.presentation.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.hiddenodds.trebolv2.R


class PdfEndTaskView: FrameLayout {
    constructor(context: Context): super(context)
    constructor(context: Context, attributeSet: AttributeSet):
            super(context, attributeSet)
    @BindView(R.id.signatureClient)
    @JvmField var signatureClient: ImageView? = null
    @BindView(R.id.lblContent)
    @JvmField var lblContent: TextView? = null

    init {

        LayoutInflater.from(context)
                .inflate(R.layout.view_pdf_end_task, this, true)
        ButterKnife.bind(this)

    }
}