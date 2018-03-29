package com.hiddenodds.trebolv2.tools

import android.app.TimePickerDialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.widget.EditText
import com.hiddenodds.trebolv2.R
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

    @Throws(NumberFormatException::class)
    fun substracHours(etIn: String, etOut: String): String{
        var difHrs = "0"

        val hrsInit = etIn.split(":".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
        val hrsEnd = etOut.split(":".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
        if (hrsInit.isNotEmpty() && hrsEnd.isNotEmpty()) {
            val minInit = (hrsInit[0].toInt() * 60) + hrsInit[1].toInt()
            val minEnd = (hrsEnd[0].toInt() * 60) + hrsEnd[1].toInt()
            val dif = minEnd - minInit
            val minWork = (dif % 60)
            val hrsWork = (dif / 60)
            difHrs = hrsWork.toString() + ":" + minWork.toString()
        }

        return difHrs
    }

    fun setTimeToControl(editText: EditText, context: Context){
        val currentTime = Calendar.getInstance()
        val hour = currentTime.get(Calendar.HOUR_OF_DAY)
        val min = currentTime.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(context,
                TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    var h = hourOfDay.toString()
                    var m = minute.toString()
                    if (hourOfDay < 10) h = "0" + hourOfDay.toString()
                    if (minute < 10) m = "0" + minute.toString()
                    val timeSelect = "$h:$m"
                    editText.setText(timeSelect)
                }, hour, min, false)
        timePickerDialog.show()

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun addDecorationRecycler(rv: RecyclerView, context: Context){
        val horizontalDecoration =
                DividerItemDecoration(rv.context,
                        DividerItemDecoration.VERTICAL)
        val horizontalDivider: Drawable = context
                .getDrawable(R.drawable.horizontal_divider)
        horizontalDecoration.setDrawable(horizontalDivider)
        rv.addItemDecoration(horizontalDecoration)
    }


}