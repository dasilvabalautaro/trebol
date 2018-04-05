package com.hiddenodds.trebolv2

import android.app.Application
import android.content.res.Configuration
import com.hiddenodds.trebolv2.dagger.AppComponent
import com.hiddenodds.trebolv2.dagger.AppModule
import com.hiddenodds.trebolv2.dagger.DaggerAppComponent
import com.hiddenodds.trebolv2.model.interfaces.IPersistent
import com.hiddenodds.trebolv2.model.persistent.network.ServiceRemote
import com.hiddenodds.trebolv2.tools.ConnectionNetwork
import com.hiddenodds.trebolv2.tools.LocaleUtils
import java.util.*
import javax.inject.Inject


class App: Application() {
    @Inject
    lateinit var iPersistent: IPersistent
    @Inject
    lateinit var serviceRemote: ServiceRemote
    @Inject
    lateinit var connectionNetwork: ConnectionNetwork
    @Inject
    lateinit var localeUtils: LocaleUtils

    companion object{
        lateinit var appComponent: AppComponent
    }

    private val component: AppComponent by lazy {
        DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        component.inject(this)
        iPersistent.create()
        localeUtils.setLocale(Locale("es"))
        localeUtils.updateConfiguration(this,
                baseContext.resources.configuration)
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        localeUtils.updateConfiguration(this, newConfig!!)
    }

    fun getAppComponent(): AppComponent{
        appComponent = component
        return appComponent
    }
}