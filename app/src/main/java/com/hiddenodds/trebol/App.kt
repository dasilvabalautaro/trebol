package com.hiddenodds.trebol

import android.app.Application
import android.content.res.Configuration
import com.hiddenodds.trebol.dagger.AppComponent
import com.hiddenodds.trebol.dagger.AppModule
import com.hiddenodds.trebol.dagger.DaggerAppComponent
import com.hiddenodds.trebol.model.interfaces.IPersistent
import com.hiddenodds.trebol.model.persistent.network.ServiceRemote
import com.hiddenodds.trebol.tools.ConnectionNetwork
import com.hiddenodds.trebol.tools.LocaleUtils
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