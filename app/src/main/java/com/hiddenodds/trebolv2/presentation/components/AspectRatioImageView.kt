package com.hiddenodds.trebolv2.presentation.components

import android.content.Context
import android.support.annotation.IntDef
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import com.hiddenodds.trebolv2.R

class AspectRatioImageView: AppCompatImageView {
    constructor(context: Context): super(context)
    constructor(context: Context, attributeSet: AttributeSet):
            super(context, attributeSet){
        start(attributeSet)
    }
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int):
            super(context, attributeSet, defStyleAttr){
        start(attributeSet)
    }

    companion object {
        const val WIDTH = 0
        const val HEIGHT = 1
    }

    val DEFAULT_RATIO = 1.toFloat()
    private val AUTO = 2

    private var aspect: Int = 0
    private var aspectRatio: Float = 0.toFloat()

    private fun start(attrs: AttributeSet){
        val a = context.obtainStyledAttributes(attrs,
                R.styleable.AspectRatioImageView)
        try {
            aspect = a.getInt(R.styleable.AspectRatioImageView_ari_aspect, HEIGHT)
            aspectRatio = a.getFloat(R.styleable
                    .AspectRatioImageView_ari_ratio, DEFAULT_RATIO)
        } finally {
            a.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int,
                                    heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val height = measuredHeight
        val width = measuredWidth

        when (aspect) {
            AUTO -> if (height > width) {
                if (width == 0) {
                    return
                }
                aspect = WIDTH
                aspectRatio = Math.round(height.toDouble() / width).toFloat()
                setMeasuredDimensionByHeight(height)
            } else {
                if (height == 0) {
                    return
                }
                aspect = HEIGHT
                aspectRatio = Math.round(width.toDouble() / height).toFloat()
                setMeasuredDimensionByWidth(width)
            }
            WIDTH -> setMeasuredDimensionByHeight(height)
            HEIGHT -> setMeasuredDimensionByWidth(width)
            else -> setMeasuredDimensionByWidth(width)
        }
    }

    private fun setMeasuredDimensionByWidth(width: Int) {
        setMeasuredDimension(width, (width * aspectRatio).toInt())
    }

    private fun setMeasuredDimensionByHeight(height: Int) {
        setMeasuredDimension((height * aspectRatio).toInt(), height)
    }

    fun getAspectRatio(): Float {
        return aspectRatio
    }

    fun setAspectRatio(ratio: Float) {
        aspectRatio = ratio
        requestLayout()
    }

    @Aspect
    fun getAspect(): Int {
        return aspect
    }

    fun setAspect(@Aspect aspect: Int) {
        this.aspect = aspect
        requestLayout()
    }

    @IntDef(WIDTH.toLong(), HEIGHT.toLong())
    @Retention(AnnotationRetention.SOURCE)
    annotation class Aspect

}