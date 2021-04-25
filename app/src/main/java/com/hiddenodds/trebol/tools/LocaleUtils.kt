package com.hiddenodds.trebol.tools

import android.annotation.SuppressLint
import android.app.Application
import android.content.res.Configuration
import android.content.res.Resources
import android.view.ContextThemeWrapper
import org.jetbrains.anko.displayMetrics
import java.util.*


class LocaleUtils {
    private var locale: Locale? = null

    fun setLocale(locale: Locale){
        this.locale = locale
        if (this.locale != null){
            Locale.setDefault(this.locale!!)
        }
    }

    fun updateConfiguration(wrapper: ContextThemeWrapper){
        if (locale != null ){
            val configuration: Configuration = Configuration()
            configuration.setLocale(locale)
            wrapper.applyOverrideConfiguration(configuration)
        }
    }

    //@SuppressLint("ObsoleteSdkInt")
    fun updateConfiguration(app: Application,
                            configurationNew: Configuration){
        if (locale != null ){
//            val resources: Resources = app.baseContext.resources
            val configuration: Configuration = Configuration(configurationNew)
            configuration.setLocale(locale)
//            val displayMetrics = resources.displayMetrics
            app.applicationContext.createConfigurationContext(configuration)

        //            val resources: Resources = app.baseContext.resources
//            configuration.densityDpi = resources.displayMetrics
////            @Suppress("DEPRECATION")
//            resources.updateConfiguration(configuration, resources.displayMetrics)
        //            app.baseContext.createConfigurationContext(configuration)
        }

    }
}