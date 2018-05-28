package com.hiddenodds.trebolv2.presentation.model

import android.graphics.Bitmap
import java.io.Serializable


class ProxyBitmap(bitmap: Bitmap): Serializable {
    private lateinit var pixels: IntArray
    private var width = 0
    private var height = 0

    init {
        try {
            width = bitmap.width
            height = bitmap.height
            pixels = IntArray(width*height)
            bitmap.getPixels(pixels, 0,
                    width, 0, 0, width, height)
        }catch (ea: ArrayIndexOutOfBoundsException){
            println(ea.message)
        }

    }

    fun getBitmap(): Bitmap{
        return Bitmap.createBitmap(pixels,
                width, height, Bitmap.Config.ARGB_8888)
    }
}