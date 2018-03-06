package com.hiddenodds.trebolv2.tools

import java.text.SimpleDateFormat
import java.util.*

object ChangeFormat {

    fun convertDate(date: String): String{
        var result = ""

        if (date.isNotEmpty()){
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val dateIn = formatter.parse(date)
            result = formatter.format(dateIn)
        }
        return result
    }
}