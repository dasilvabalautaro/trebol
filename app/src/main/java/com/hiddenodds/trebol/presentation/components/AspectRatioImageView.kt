package com.hiddenodds.trebol.presentation.components

import android.content.Context
import androidx.annotation.IntDef
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.hiddenodds.trebol.R
import kotlin.math.roundToInt

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
                aspectRatio = (height.toDouble() / width).roundToInt().toFloat()
                setMeasuredDimensionByHeight(height)
            } else {
                if (height == 0) {
                    return
                }
                aspect = HEIGHT
                aspectRatio = (width.toDouble() / height).roundToInt().toFloat()
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

    @IntDef(WIDTH.toLong().toInt(), HEIGHT.toLong().toInt())
    @Retention(AnnotationRetention.SOURCE)
    annotation class Aspect

}