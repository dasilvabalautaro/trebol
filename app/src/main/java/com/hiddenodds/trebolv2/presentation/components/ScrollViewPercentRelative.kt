package com.hiddenodds.trebolv2.presentation.components

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.widget.ScrollView

class ScrollViewPercentRelative: ScrollView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attributes: AttributeSet) :
            super(context, attributes)
    constructor(context: Context, attributes: AttributeSet, defStyleAttr: Int) :
            super(context, attributes, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attributes: AttributeSet,
                defStyleAttr: Int, defStyleRes: Int) :
            super(context, attributes, defStyleAttr, defStyleRes)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val width = measuredWidth
        val height = measuredHeight
        setMeasuredDimension(width, height)
    }
}