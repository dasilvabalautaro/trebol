@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATION")

package com.hiddenodds.trebol.tools

import android.app.TimePickerDialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.percentlayout.widget.PercentLayoutHelper
import android.view.View
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.hiddenodds.trebol.R
import com.hiddenodds.trebol.model.persistent.caching.CachingLruRepository
import com.hiddenodds.trebol.tools.PreferenceHelper.get
import com.hiddenodds.trebol.tools.PreferenceHelper.set
import java.text.SimpleDateFormat
import java.util.*
import kotlin.NoSuchElementException

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

    fun convertStringToDate(date: String): java.sql.Date{
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        return java.sql.Date(formatter.parse(date).time)

    }

    @Throws(NumberFormatException::class)
    fun substracHours(etIn: String, etOut: String): String{
        var difHrs = "0"

        val hrsInit = etIn.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val hrsEnd = etOut.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (hrsInit.isNotEmpty() && hrsEnd.isNotEmpty()) {
            val minInit = (hrsInit[0].toInt() * 60) + hrsInit[1].toInt()
            val minEnd = (hrsEnd[0].toInt() * 60) + hrsEnd[1].toInt()
            val dif = minEnd - minInit
            val minWork = (dif % 60)
            val hrsWork = (dif / 60)
            var minModify = 0
            when(minWork){
                in 1..15 -> minModify = 25
                in 16..30 -> minModify = 50
                in 31..45 -> minModify = 75
                in 46..59 -> hrsWork + 1
            }
            difHrs = "$hrsWork.$minModify"
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
                    if (hourOfDay < 10) h = "0$hourOfDay"
                    if (minute < 10) m = "0$minute"
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
        val horizontalDivider: Drawable? = ContextCompat
                .getDrawable(context, R.drawable.horizontal_divider)
        if (horizontalDivider != null) {
            horizontalDecoration.setDrawable(horizontalDivider)
        }
        rv.addItemDecoration(horizontalDecoration)
    }

    //@Suppress("DEPRECATION")
    fun setHeightPercent(view: View, height: Float){
        val params = view.layoutParams as PercentLayoutHelper.PercentLayoutParams
        val info = params.percentLayoutInfo
        info.heightPercent = height
        view.requestLayout()
    }

    @Suppress("DEPRECATION")
    fun setWidthPercent(view: View, width: Float){
        val params = view.layoutParams as PercentLayoutHelper.PercentLayoutParams
        val info = params.percentLayoutInfo
        info.widthPercent = width
        view.requestLayout()
    }

    @Suppress("DEPRECATION")
    fun setWidthPercentLandscape(view: View){
        val params = view.layoutParams as PercentLayoutHelper.PercentLayoutParams
        val info = params.percentLayoutInfo
        info.widthPercent = info.heightPercent * 0.5f
        view.requestLayout()
    }

    @Suppress("DEPRECATION")
    fun setLeftPercent(view: View, left: Float){
        val params = view.layoutParams as PercentLayoutHelper.PercentLayoutParams
        val info = params.percentLayoutInfo
        info.leftMarginPercent = left
        view.requestLayout()
    }

    @Suppress("DEPRECATION")
    fun setRightPercent(view: View, right: Float){
        val params = view.layoutParams as PercentLayoutHelper.PercentLayoutParams
        val info = params.percentLayoutInfo
        info.rightMarginPercent = right
        view.requestLayout()
    }

    fun setVariablesConnect(context: Context){
        try {
            val prefs = PreferenceHelper.customPrefs(context,
                    Constants.PREFERENCE_TREBOL)
            val sqlServerKey: String? = prefs[Constants.SQLSERVER, ""]
            if (sqlServerKey.isNullOrEmpty()){
                prefs[Constants.SQLSERVER] = Variables.sqlServer
                prefs[Constants.DATABASE] = Variables.database
                prefs[Constants.USER] = Variables.user
                prefs[Constants.PASSWORD] = Variables.password
            }else{
                Variables.sqlServer = prefs[Constants.SQLSERVER, ""]!!
                Variables.database = prefs[Constants.DATABASE, ""]!!
                Variables.user = prefs[Constants.USER, ""]!!
                Variables.password = prefs[Constants.PASSWORD, ""]!!
            }

        }catch (ie: IllegalStateException){
            println(ie.message)
        }

    }

    fun changeVariablesConnect(context: Context){
        val prefs = PreferenceHelper.customPrefs(context,
                Constants.PREFERENCE_TREBOL)
        prefs[Constants.SQLSERVER] = Variables.sqlServer
        prefs[Constants.DATABASE] = Variables.database
        prefs[Constants.USER] = Variables.user
        prefs[Constants.PASSWORD] = Variables.password

    }

    fun refreshVariablesconnect(context: Context){
        val prefs = PreferenceHelper.customPrefs(context,
                Constants.PREFERENCE_TREBOL)
        Variables.sqlServer = prefs[Constants.SQLSERVER, ""]!!
        Variables.database = prefs[Constants.DATABASE, ""]!!
        Variables.user = prefs[Constants.USER, ""]!!
        Variables.password = prefs[Constants.PASSWORD, ""]!!
    }

    fun deleteCacheTechnical(codeTech: String){

        try {
            if (Variables.changeTechnical.isNotEmpty()){

                val code = Variables.changeTechnical.first { it == codeTech }
                if (code.isNotEmpty() && code == codeTech){
                    Variables.changeTechnical.remove(code)
                    CachingLruRepository
                            .instance
                            .getLru()
                            .remove(codeTech)
                }

            }

        }catch (ee: NoSuchElementException){
            println(ee.message)
        }
    }

    fun deleteCache(key: String){
        try {
            CachingLruRepository
                    .instance
                    .getLru()
                    .remove(key)
        }catch (ex: Exception){
            println(ex.message)
        }
    }
}