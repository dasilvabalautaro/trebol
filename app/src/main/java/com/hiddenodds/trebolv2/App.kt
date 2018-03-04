package com.hiddenodds.trebolv2

import android.app.Application
import com.hiddenodds.trebolv2.dagger.AppComponent
import com.hiddenodds.trebolv2.dagger.AppModule
import com.hiddenodds.trebolv2.dagger.DaggerAppComponent
import com.hiddenodds.trebolv2.model.interfaces.IPersistent
import com.hiddenodds.trebolv2.model.persistent.network.ServiceRemote
import com.hiddenodds.trebolv2.tools.ConnectionNetwork
import javax.inject.Inject


class App: Application() {
    @Inject
    lateinit var iPersistent: IPersistent
    @Inject
    lateinit var serviceRemote: ServiceRemote
    @Inject
    lateinit var connectionNetwork: ConnectionNetwork

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
        serviceRemote.connection()
        iPersistent.create()
    }

    fun getAppComponent(): AppComponent{
        appComponent = component
        return appComponent
    }
}